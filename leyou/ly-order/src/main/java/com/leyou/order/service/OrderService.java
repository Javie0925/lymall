package com.leyou.order.service;

import com.github.wxpay.sdk.WXPay;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.IdWorker;
import com.leyou.item.pojo.Sku;
import com.leyou.order.client.SkuClient;
import com.leyou.order.dto.CartDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.enums.OrderStatusEnum;
import com.leyou.order.enums.PayStatusEnum;
import com.leyou.order.helper.PayHelper;
import com.leyou.order.interceptor.UserInterceptor;
import com.leyou.order.mapper.AddressMapper;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Address;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author javie
 * @date 2019/6/1 14:27
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private PayHelper payHelper;
    @Autowired
    private WXPay wxPay;

    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        // 获取收货地址
        Address addr = addressMapper.selectByPrimaryKey(orderDTO.getAddressId());
        // 获取商品sku
        List<Long> ids = new ArrayList<>();
        orderDTO.getCarts().forEach(cart -> {
            ids.add(cart.getSkuId());
        });
        List<Sku> skuList = skuClient.querySkuByIds(ids);
        // 创建order类
        long orderId = idWorker.nextId();
        Order order = new Order();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());
        // 用户信息
        UserInfo user = UserInterceptor.getUserInfo();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);
        // 收货人信息
        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        order.setReceiverState(addr.getState());
        order.setReceiverZip(addr.getZipCode());
        // 商品信息
        Map<Long, Integer> numMap = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        long totalPay = 0L;
        // orderDetails 集合
        List<OrderDetail> details = new ArrayList<>();
        for (Sku sku:skuList){
            // 累加计算总金额
            totalPay+=sku.getPrice()*numMap.get(sku.getId());
            // 分装orderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setSkuId(sku.getId());
            orderDetail.setOrderId(orderId);
            orderDetail.setNum(numMap.get(sku.getId()));
            orderDetail.setTitle(sku.getTitle());
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setPrice(sku.getPrice());
            orderDetail.setImage(sku.getImages());
            details.add(orderDetail);

        }
        // 总金额
        order.setTotalPay(totalPay);
        // 实付金额
        order.setActualPay(totalPay+order.getPostFee());
        // 写入数据库
        int insert = orderMapper.insertSelective(order);
        if (insert!=1) {
            log.error("[创建订单]，订单创建失败，订单号：{}",orderId);
            throw new LyException(ExceptionEnmu.ORDER_CREATED_ERROR);
        }
        // 将orderDetail写入数据库
        details.forEach(detail->{
            int insert1 = orderDetailMapper.insert(detail);
            if (insert1!=1) {
                log.error("[创建订单]，订单创建失败，订单号：{}",orderId);
                throw new LyException(ExceptionEnmu.ORDER_CREATED_ERROR);
            }
        });
        // 创建OrderStatus类
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UNPAY.value());
        int insert1 = orderStatusMapper.insert(orderStatus);
        if (insert1!=1) {
            log.error("[创建订单]，订单创建失败，订单号：{}",orderId);
            throw new LyException(ExceptionEnmu.ORDER_CREATED_ERROR);
        }
        // 返回订单号
        return orderId;
    }

    public Order queryOrderById(Long orderId){
        // 获取order
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order==null){
            throw new LyException(ExceptionEnmu.ORDER_NOT_FOUND);
        }
        // 获取orderDetail
        OrderDetail record = new OrderDetail();
        record.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(record);
        order.setOrderDetails(orderDetails);
        // 获取orderStatus
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        order.setStatus(orderStatus.getStatus());
        return order;
    }

    public String createPayUrl(Long orderId) {
        // 查询订单
        Order order = queryOrderById(orderId);
        // 判断订单状态
        if (order.getStatus()!=1){
            throw new LyException(ExceptionEnmu.ORDER_STATUS_ERROR);
        }
        // 获取支付金额
        Long actualPay = order.getActualPay();
        // 获取商品描述
        StringBuffer desc = new StringBuffer();
        order.getOrderDetails().forEach(detail->{
            desc.append(detail.getTitle()+";");
        });
        String url = payHelper.createOrder(orderId, actualPay, desc.toString());
        return url;
    }

    public void handleNotify(Map<String, String> result) {
        // 判断通信和业务标识
        payHelper.isSuccess(result);
        // 验证签名
        payHelper.isValidSign(result);
        // 校验金额
        String totalFeeStr = result.get("total_fee");
        String tradeNo = result.get("out_trade_no");
        if (StringUtils.isEmpty(totalFeeStr)||StringUtils.isEmpty(tradeNo)){
            throw new LyException(ExceptionEnmu.INVALID_ORDER_PARAM);
        }
        long totalFee = Long.valueOf(totalFeeStr);
        // 获取订单id
        Long orderID = Long.valueOf(tradeNo);
        Order order = orderMapper.selectByPrimaryKey(orderID);
        if(totalFee != 1l){
            throw new LyException(ExceptionEnmu.INVALID_ORDER_PARAM);
        }
        // 校验成功，修改订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(order.getOrderId());
        orderStatus.setStatus(OrderStatusEnum.PAYED.value());
        orderStatus.setPaymentTime(new Date());
        // 写入数据库
        int update = orderStatusMapper.updateByPrimaryKey(orderStatus);
        if (update!=1){
            throw new LyException(ExceptionEnmu.UPDATE_ORDER_STATUS_ERROR);
        }
        log.info("[订单回调] 订单支付成功", order.getOrderId());
    }

    public PayStatusEnum queryOrderStatusById(Long orderId) {

        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        Integer status = orderStatus.getStatus();
        if(status!=PayStatusEnum.NOT_PAY.getValue()){
            return PayStatusEnum.SUCCESS;
        }
        // 如果未支付，不一定是没支付，要去微信查询状态

        return null;
    }
    public PayStatusEnum queryPayStatus(Long orderID){
        // 请求参数
        Map<String,String> data = new HashMap<>();
        data.put("out_trade_no", orderID+"");
        try {
            Map<String, String> result = wxPay.orderQuery(data);
            // 校验通信状态

        } catch (Exception e) {
            return PayStatusEnum.NOT_PAY;
        }
        return null;
    }
}
