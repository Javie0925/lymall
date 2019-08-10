package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author javie
 * @date 2019/5/29 1:20
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
