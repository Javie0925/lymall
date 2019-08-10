package com.leyou.user.service;

import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author javie
 * @date 2019/5/28 15:38
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone:";

    public Boolean checkData(String data, Integer type) {

        User record = new User();
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnmu.USER_DATA_TYPE_ERROR);
        }
        int count = userMapper.selectCount(record);
        return count!=1;
    }

    public void sendCode(String phone) {

        // 验证手机号码
        String regex = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        if (!phone.matches(regex)){
            throw new LyException(ExceptionEnmu.USER_DATA_TYPE_ERROR);
        }
        //生成key
        String key = KEY_PREFIX+phone;
        // 生成验证码
        String code = NumberUtils.generateCode(6);

        Map<String,String> msg = new HashMap<>();
        msg.put("code", code);
        msg.put("phone", phone);
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
        // 保存验证码到redis
        stringRedisTemplate.opsForValue().set(key, code, 5L, TimeUnit.MINUTES);
    }

    @Transactional
    public void register(User user, String code) {
        // 校验验证码
        String codeInRedis = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!StringUtils.equals(code, codeInRedis)){
            throw new LyException(ExceptionEnmu.INVALID_VERIFY_CODE);
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        // 加密密码
        String md5Password = CodecUtils.md5Hex(user.getPassword(), salt);
        // 补全数据
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreated(new Date());
        // 保存用户信息
        userMapper.insert(user);

    }

    public User queryUser(String username, String password) {

        User user = new User();
        user.setUsername(username);
        User result = userMapper.selectOne(user);
        // 校验用户名
        if (result==null){
            throw new LyException(ExceptionEnmu.INVALID_USERNAME);
        }
        // 校验密码
        if(StringUtils.equals(result.getPassword(), CodecUtils.md5Hex(password, result.getSalt()))){
            throw new LyException(ExceptionEnmu.INVALID_PASSWORD);
        }
        // 校验通过，返回数据
        return result;
    }
}
