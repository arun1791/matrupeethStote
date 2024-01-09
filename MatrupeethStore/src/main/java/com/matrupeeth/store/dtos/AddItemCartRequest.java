package com.matrupeeth.store.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AddItemCartRequest {
    private String productId;
    private int quantity;
}
