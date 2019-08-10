package com.leyou.cart.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author javie
 * @date 2019/5/30 14:42
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private JwtProperties props;

    public UserInterceptor(JwtProperties props) {
        this.props = props;
    }

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, props.getPublicKey());
            // 传递用户
            // request.setAttribute("userInfo", userInfo);
            // springmvc 不建议用域对象传递数据，故使用ThreadLocal对象传递信息
            THREAD_LOCAL.set(userInfo);
        }catch(Exception e){
            log.error("[购物车服务] 解析用户身份失败", e);
            return false;
        }
        return true;
    }

    public static UserInfo getUserInfo(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 用完数据，清空数据
        THREAD_LOCAL.remove();
    }
}
