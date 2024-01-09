package com.matrupeeth.store.dtos;

import com.matrupeeth.store.entities.CartItem;
import com.matrupeeth.store.entities.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CartDto {

    private  String cartId;

    private Date createdAt;
    private UserDto user;
    private List<CartItemDto> items=new ArrayList<CartItemDto>();

}
