package com.matrupeeth.store.dtos;

import com.matrupeeth.store.valiadte.ImageNamevalid;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private  String userId;
    @Size(min = 3,max=20,message = "invalid name !!")
    private  String name;
//    @Email(message="Invalid user email id")
    @Pattern(regexp ="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" ,message="Invalid user email id !!")
    @NotBlank(message = "email must required !!")
    private String email;
    @NotBlank(message = "password is required!!")
    private String password;
    @Size(min=4,max=6,message = "invalid gender !!")
    private String gender;
    @NotBlank(message ="write something about yourself !!")
    private  String about;
    @ImageNamevalid
    private  String imageName;
}
