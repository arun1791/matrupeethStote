package com.matrupeeth.store.dtos;

import com.matrupeeth.store.entities.Category;
import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {
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
    private CategoryDto category;
}
