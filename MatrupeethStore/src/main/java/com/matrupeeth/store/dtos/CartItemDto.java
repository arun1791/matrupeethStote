package com.matrupeeth.store.dtos;

import com.matrupeeth.store.entities.Cart;
import com.matrupeeth.store.entities.Product;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartItemDto {

    private  int cartItemId;

    private ProductDto product;
    private  int quantity;
    private  int totalPrice;
//    private CartDto cart;
}
