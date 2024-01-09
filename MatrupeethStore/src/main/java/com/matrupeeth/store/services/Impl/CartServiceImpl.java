package com.matrupeeth.store.services.Impl;

import com.matrupeeth.store.dtos.AddItemCartRequest;
import com.matrupeeth.store.dtos.CartDto;
import com.matrupeeth.store.entities.Cart;
import com.matrupeeth.store.entities.CartItem;
import com.matrupeeth.store.entities.Product;
import com.matrupeeth.store.entities.User;
import com.matrupeeth.store.exception.BadApiRequest;
import com.matrupeeth.store.exception.ResourcesNotFoundException;
import com.matrupeeth.store.repositories.CartItemRepository;
import com.matrupeeth.store.repositories.CartRepository;
import com.matrupeeth.store.repositories.ProductRepository;
import com.matrupeeth.store.repositories.UserRepository;
import com.matrupeeth.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CartDto addItemCart(String userId, AddItemCartRequest request) {
        int quantity=request.getQuantity();
        String productId=request.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourcesNotFoundException("Product not found"));
        //fetach the user db
       if (quantity<=0)
       {
           throw  new BadApiRequest("Request quantity is out of range");
       }
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User not found"));
        Cart cart = null;
        try {
           cart= cartRepository.findByUser(user).get();
        }catch (Exception e) {
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());

        }
        AtomicBoolean updated= new AtomicBoolean(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
//        cart.setItems(updateItems);

        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
            //perform cart opration
        }

        cart.setUser(user);
        Cart updateCart = cartRepository.save(cart);
        return modelMapper.map(updateCart,CartDto.class);
    }



    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        //condtions

        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourcesNotFoundException("Cart not found"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourcesNotFoundException("user not found "));
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourcesNotFoundException("user not found "));
       return  modelMapper.map(cart,CartDto.class);

    }
}
