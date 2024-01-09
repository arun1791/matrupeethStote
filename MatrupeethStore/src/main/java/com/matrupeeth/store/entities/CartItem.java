package com.matrupeeth.store.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private  Product product;
    private  int quantity;
    private  int totalPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private  Cart cart;

}
