package com.matrupeeth.store.services;

import com.matrupeeth.store.dtos.CreateOrderRequest;
import com.matrupeeth.store.dtos.OrderDto;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderService  {
    //crete order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);
    //get order of user

    //get order
    List<OrderDto> getOrdersOfUser(String userId);
    PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sordtDir);

    //other methods

}
