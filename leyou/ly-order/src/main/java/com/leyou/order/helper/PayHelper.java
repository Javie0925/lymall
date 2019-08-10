package com.leyou.order.helper;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.order.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/1 21:54
 */
@Slf4j
@Component
public class PayHelper {

    @Autowired
    private WXPay wxPay;
    @Autowired
    private PayConfig config;

    public String createOrder(Long orderId,Long totalPay,String desc){
        try{
            Map<String,String> data = new HashMap<>();
            // 商品描述
            data.put("body", "leyoumall");
            // 订单号
            data.put("out_trade_no", orderId.toString());
            // 金额，分
            data.put("total_fee", 1+"");
            // 调用微信支付终端ip
            data.put("spbill_create_ip", "127.0.0.1");
            // 回调地址
            data.put("notify_url", config.getNotifyUrl());

            // 交易类型为扫码支付
            data.put("trade_type", "NATIVE");
            Map<String,String> result = wxPay.unifiedOrder(data);

            // 打印结果
            for(Map.Entry<String,String> entry:result.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key+":"+value);
            }
            isSuccess(result);


            // 下单成功，获取支付连接
            String url = result.get("code_url");
            return url;
        }catch (Exception e){
            log.error("[微信下单] 创建预交易订单失败", e);
            return null;
        }
    }

    public void isSuccess(Map<String, String> result) {
        // 判断通信标识
        if (WXPayConstants.FAIL.equals(result.get("retun_code"))) {
            // 通信失败
            log.error("[微信下单] 通信失败，失败原因{}",result.get("return_msg"));
            throw new LyException(ExceptionEnmu.WXPAY_PAY_CODE_CREATED_ERROR);
        }

        //判断业务标识
        if (WXPayConstants.FAIL.equals(result.get("result_code"))) {
            // 通信失败
            log.error("[微信下单] 业务失败，错误码{}，失败原因{}",result.get("err_code"),result.get("err_code_desc"));
            throw new LyException(ExceptionEnmu.WXPAY_PAY_CODE_CREATED_ERROR);
        }
    }

    public void isValidSign(Map<String, String> data) {
        try {
            // 重新生成签名
            String hma = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.HMACSHA256);
            String md5 = WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5);
            // 和传过来的签名进行比较
            String sign = data.get("sign");
            if( hma.equals(sign) || md5.equals(sign)){
                // 签名有误，抛出异常
                throw new LyException(ExceptionEnmu.INVALID_SIGN);
            }
        } catch (Exception e) {
            throw new LyException(ExceptionEnmu.INVALID_SIGN);
        }
    }
}
