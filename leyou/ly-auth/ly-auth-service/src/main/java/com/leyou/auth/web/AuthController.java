package com.leyou.auth.web;

        import com.leyou.auth.config.JwtProperties;
        import com.leyou.auth.pojo.UserInfo;
        import com.leyou.auth.service.AuthService;
        import com.leyou.auth.utils.JwtUtils;
        import com.leyou.common.enmus.ExceptionEnmu;
        import com.leyou.common.exception.LyException;
        import com.leyou.common.utils.CookieUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.context.properties.EnableConfigurationProperties;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;

/**
 * @author javie
 * @date 2019/5/29 0:16
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response){

        String token = authService.login(username,password);
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token );
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN")String token,
            HttpServletRequest request,
            HttpServletResponse response
    ){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 刷新token，重新生成token
            String newToken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            // 写入cookie
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), newToken);
            // 已登录，返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            // token过期，或者token被篡改
            throw new LyException(ExceptionEnmu.UN_AUTHORIZED);
        }
    }
}
