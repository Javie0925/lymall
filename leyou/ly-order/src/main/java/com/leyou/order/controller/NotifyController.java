package com.leyou.order.controller;

import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/2 2:05
 */
@Slf4j
@RestController
@RequestMapping("notify")
public class NotifyController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String hello(){
        return "hello!";
    }
    /**
     * 微信支付的成功回调
     */
    @PostMapping(value = "/pay",produces = "application/xml")
    public Map<String,String> notify(@RequestBody Map<String,String> result){
        // 处理回调信息
        orderService.handleNotify(result);

        log.info("[支付回调] 接收微信支付回调，结果：", result);

        // 返回成功
        Map<String,String> msg = new HashMap<>();
        msg.put("return_code", "SUCCESS");
        msg.put("return_msg", "OK");
        return msg;
    }
}
