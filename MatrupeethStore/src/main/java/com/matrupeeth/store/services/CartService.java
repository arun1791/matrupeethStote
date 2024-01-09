package com.matrupeeth.store.services;

import com.matrupeeth.store.dtos.AddItemCartRequest;
import com.matrupeeth.store.dtos.CartDto;

public interface CartService {

    CartDto addItemCart(String userId, AddItemCartRequest request);
    void removeItemFromCart(String userId, int cartItem);

    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}
