package com.leyou.order.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.order.interceptor.UserInterceptor;
import com.leyou.order.mapper.AddressMapper;
import com.leyou.order.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/31 19:36
 */
@Service
public class AddrService {

    @Autowired
    private AddressMapper addressMapper;

    public List<Address> queryAddrByUserId(){
        // 获取用户信息
        UserInfo user = UserInterceptor.getUserInfo();
        // 根据用户id获取用户地址列表
        Address record = new Address();
        record.setUserId(user.getId());
        List addrList = addressMapper.select(record);
        // 判断是否为空
        if (CollectionUtils.isEmpty(addrList)) {
            throw new LyException(ExceptionEnmu.EMPTY_USER_ADDRESS);
        }
        // 返回地址列表
        return addrList;
    }
}
