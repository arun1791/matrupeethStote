package com.matrupeeth.store.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "oder_items")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    private int quantity;
    private  int totalPrice;
    @OneToOne
    private Product product;
    @ManyToOne
    private  Order order;

}
