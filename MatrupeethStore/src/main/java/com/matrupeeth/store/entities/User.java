package com.matrupeeth.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matru_users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    @Id
    private  String userId;
    @Column(name = "user_name",unique = true)
    private  String name;
    @Column(name = "user_email",unique = true)
    private String email;
    @Column(length = 10)
    private String password;
    private String gender;
    private  String about;
    @Column(name = "user_image_name")
    private  String imageName;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Order>orders=new ArrayList<Order>();
}
