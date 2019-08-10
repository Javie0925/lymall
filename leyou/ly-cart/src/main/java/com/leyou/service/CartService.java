package com.leyou.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.enmus.ExceptionEnmu;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javie
 * @date 2019/5/30 15:35
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "cart:user:id";

    public void addCart(Cart cart) {
        // 获取当前用户
        UserInfo user = UserInterceptor.getUserInfo();
        // 判断当前购物车商品是否存在
        String key = KEY_PREFIX + user.getId();
        String field = cart.getSkuId()+"";
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(key);
        if (ops.hasKey(field)) {
            // 存在，修改数量
            String jsonCart = ops.get(field).toString();
            Cart cacheCart = JsonUtils.toBean(jsonCart, Cart.class);
            cacheCart.setNum(cacheCart.getNum()+cart.getNum());
            ops.put(field, JsonUtils.serialize(cacheCart));
        }else{
            // 否，增加商品
            ops.put(field, JsonUtils.serialize(cart));
        }
    }

    public List<Cart> loadCart() {
        // 获取用户信息
        UserInfo user = UserInterceptor.getUserInfo();
        // 判断购物车是否存在
        if (!stringRedisTemplate.hasKey(KEY_PREFIX+user.getId())) {
            // 不存在,返回空
            return null;
        }
        // 存在，取出购物车信息
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(KEY_PREFIX + user.getId());
        List<Object> values = hashOps.values();
        // 判断购物车是否有物品
        if (!CollectionUtils.isEmpty(values)){
            List<Cart> cartList = new ArrayList<>();
            values.forEach(v->{
                String json = (String)v;
                cartList.add(JsonUtils.toBean(json, Cart.class));
            });
            return cartList;
        }
        // 购物车为空，返回null
        return null;
    }

    public void updateCart(Long id, Integer num) {
        // 获取用户信息
        UserInfo user = UserInterceptor.getUserInfo();
        // 获取购物车商品
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(KEY_PREFIX + user.getId());
        String json = (String)ops.get(id+"");
        // 更新商品信息
        Cart cart = JsonUtils.toBean(json, Cart.class);
        cart.setNum(num);
        // 保存商品信息
        ops.put(id+"", JsonUtils.serialize(cart));
    }

    public void deleteCart(Long id) {
        UserInfo user = UserInterceptor.getUserInfo();
        String key = KEY_PREFIX+user.getId();
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(key);
        try {
            ops.delete(id+"");
        }catch(Exception e){
            throw new LyException(ExceptionEnmu.CART_NOT_FOUND);
        }
    }
}
