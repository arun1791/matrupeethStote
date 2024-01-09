package com.matrupeeth.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {
    @NotBlank(message = "Id is must ")
    private  String cartId;
    @NotBlank(message = "Id is must ")
    private  String userId;

    //pending , de
    private String orderStatus="PENDING";
    //not paid ,paid
    private String paymentStatus="NOTPAID";
    @NotBlank(message = "billingAddress is must ")
    private String billingAddress;
    @NotBlank(message = "billingPhone is must ")
    private  String billingPhone;
    @NotBlank(message = "billingName is must ")
    private String billingName;
    private Date deliveredDate;
    //user
//

}
