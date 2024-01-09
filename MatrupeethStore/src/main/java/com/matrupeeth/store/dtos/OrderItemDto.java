package com.matrupeeth.store.dtos;

import com.matrupeeth.store.entities.Order;
import com.matrupeeth.store.entities.Product;
import lombok.*;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private  int totalPrice;
    private ProductDto product;
//    private OrderDto order;
}
