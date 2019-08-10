package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author javie
 * @date 2019/5/29 0:18
 */
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;

    public String login(String username, String password) {
        try {
            // 校验用户信息
            User user = userClient.queryUser(username, password);
            if (user==null){
                throw new LyException(ExceptionEnmu.INVALID_USERNAME_PASSWORD);
            }
            // 生成token
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());
            String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return token;
        }catch(Exception e){
            log.error("[授权中心] 生成token失败，用户名：{}",username,e);
            throw new LyException(ExceptionEnmu.TOKEN_CREAT_ERROR);
        }
    }
}
