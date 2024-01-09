package com.matrupeeth.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Order {
    @Id
    private  String orderId;
    //pending , de
    private String orderStatus;
    //not paid ,paid
    private String paymentStatus;

    private  int orderAmount;
    @Column(length = 2500)
    private String billingAddress;
    private  String billingPhone;

    private String billingName;
    private Date orderDate;
    private Date deliveredDate;
    //user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();





}
