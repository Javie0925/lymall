package com.leyou.order.controller;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.pojo.Address;
import com.leyou.order.service.AddrService;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/31 19:45
 */
@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private AddrService addrService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/address")
    public ResponseEntity<List<Address>> loadAddr(){
        return ResponseEntity.ok(addrService.queryAddrByUserId());
    }
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO){

        Long orderId = orderService.createOrder(orderDTO);
        return ResponseEntity.ok("="+orderId);
    }
    @GetMapping("/url/{orderId}")
    public ResponseEntity<String> createPayUrl(@PathVariable("orderId")Long orderId){

        String url = orderService.createPayUrl(orderId);
        return ResponseEntity.ok(url);
    }
    @GetMapping("/state/{orderID}")
    public ResponseEntity<Integer> queryOrderStatue(@PathVariable("orderID") Long orderId){
        return ResponseEntity.ok(orderService.queryOrderStatusById(orderId).getValue());
    }
}
