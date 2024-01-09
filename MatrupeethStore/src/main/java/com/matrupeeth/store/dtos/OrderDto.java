package com.matrupeeth.store.dtos;

import com.matrupeeth.store.entities.OrderItem;
import com.matrupeeth.store.entities.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class OrderDto {
    private  String orderId;
    //pending , de
    private String orderStatus="PENDING";
    //not paid ,paid
    private String paymentStatus="NOTPAID";
    private  int orderAmount;
    private String billingAddress;
    private  String billingPhone;
    private String billingName;
    private Date orderDate=new Date();
    private Date deliveredDate;
    //user
    private UserDto user;
    private List<OrderItemDto> orderItems=new ArrayList<>();
}
