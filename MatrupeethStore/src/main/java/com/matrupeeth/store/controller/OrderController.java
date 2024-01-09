package com.matrupeeth.store.controller;

import com.matrupeeth.store.dtos.ApiResponseMassege;
import com.matrupeeth.store.dtos.CreateOrderRequest;
import com.matrupeeth.store.dtos.OrderDto;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Validated @RequestBody CreateOrderRequest request)
    {
        OrderDto order = orderService.createOrder(request);
        return  new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public  ResponseEntity<ApiResponseMassege>deleteOrder(@PathVariable String orderId)
    {
        orderService.removeOrder(orderId);
        ApiResponseMassege resposne = ApiResponseMassege.builder()
                .message("deleetd sucnsbgluu")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return  new ResponseEntity<>(resposne,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public  ResponseEntity<List<OrderDto>>getOrderOfUser(@PathVariable String userId)
    {
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return  new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<PageableResponse<OrderDto>>getAllOrder(
    @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber ,
    @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize ,
    @RequestParam(value="sortBy",defaultValue = "orderDate",required = false) String sortBy ,
    @RequestParam(value="sortDir",defaultValue = "desc",required = false) String sortDir
    )
    {
        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allOrders,HttpStatus.OK);
    }

}
