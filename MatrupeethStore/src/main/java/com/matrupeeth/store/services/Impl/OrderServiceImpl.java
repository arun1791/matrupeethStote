package com.matrupeeth.store.services.Impl;

import com.matrupeeth.store.dtos.CreateOrderRequest;
import com.matrupeeth.store.dtos.OrderDto;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.entities.*;
import com.matrupeeth.store.exception.BadApiRequest;
import com.matrupeeth.store.exception.ResourcesNotFoundException;
import com.matrupeeth.store.helper.Helper;
import com.matrupeeth.store.repositories.CartRepository;
import com.matrupeeth.store.repositories.OrderRepository;
import com.matrupeeth.store.repositories.ProductRepository;
import com.matrupeeth.store.repositories.UserRepository;
import com.matrupeeth.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        //featch User
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User not found"));
        //frec cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourcesNotFoundException("cartId not found "));
        List<CartItem> cartItems = cart.getItems();
        if(cartItems.size() <= 0)
        {
            throw new BadApiRequest("Invalid number of items cart");
        }
        //other cheks
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(orderDto.getDeliveredDate())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

            //otermitem ,amout
        AtomicReference<Integer> orderAmout = new AtomicReference<>(0);
        List<OrderItem> orderItem = cartItems.stream().map(cartItem -> {
//            CartItem->ordrItem
               OrderItem orderItem1= OrderItem.builder()
                        .quantity(cartItem.getQuantity())
                        .product(cartItem.getProduct())
                        .totalPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice())
                        .order(order)
                        .build();
               orderAmout.set(orderAmout.get()+orderItem1.getTotalPrice());

            return  orderItem1;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItem);
        order.setOrderAmount(orderAmout.get());

        cart.getItems().clear();
        cartRepository.save(cart);
        Order saveOrder = orderRepository.save(order);
        return modelMapper.map(saveOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourcesNotFoundException("Could not find order with id " + orderId));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtso = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtso;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sordtDir) {
        Sort sort=(sordtDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page,OrderDto.class);
    }
}
