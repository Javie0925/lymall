package com.leyou.web;

import com.leyou.cart.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javie
 * @date 2019/5/30 15:34
 */
@RestController
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/list")
    public ResponseEntity<List<Cart>> loadCart(){

        List<Cart> cartList = cartService.loadCart();
        return ResponseEntity.ok(cartList);
    }
    @PutMapping
    public ResponseEntity<Void> updateCart(@RequestParam("id")Long id,@RequestParam("num")Integer num){

        cartService.updateCart(id,num);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id")Long id){
        cartService.deleteCart(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
