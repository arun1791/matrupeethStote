package com.matrupeeth.store.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    private  String productId;
    private String title;
    @Column(length = 10000)
    private String description;
    private  int price;
    private  int discountedPrice;
    private  int quantity;
    private Date addedDate;
    private  boolean live;
    private  boolean    stock;
    private String productImageName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private  Category category;






}
