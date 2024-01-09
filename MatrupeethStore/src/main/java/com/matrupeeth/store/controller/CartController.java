package com.matrupeeth.store.controller;

import com.matrupeeth.store.dtos.AddItemCartRequest;
import com.matrupeeth.store.dtos.ApiResponseMassege;
import com.matrupeeth.store.dtos.CartDto;
import com.matrupeeth.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    //add the item cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemCartRequest request)
    {
        CartDto cartDto = cartService.addItemCart(userId, request);
        return  new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public  ResponseEntity<ApiResponseMassege>removeItemFromCart(@PathVariable int itemId,@PathVariable String userId)
    {
        cartService.removeItemFromCart(userId, itemId);
        ApiResponseMassege response = ApiResponseMassege.builder()
                .message("item deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.OK);

    }
    @DeleteMapping("/{userId}")
    public  ResponseEntity<ApiResponseMassege>clearCart(@PathVariable String userId)
    {
        cartService.clearCart(userId);
        ApiResponseMassege response = ApiResponseMassege.builder()
                .message("cart cleread ")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId)
    {
        CartDto cartDto = cartService.getCartByUser(userId);
        return  new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
