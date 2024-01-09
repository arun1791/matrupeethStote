package com.matrupeeth.store.repositories;

import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

//    Optional<User> findByEmail(String email);

    Optional<User> findByEmail(String email);
    List<User> findByNameContaining(String keywords);
}
