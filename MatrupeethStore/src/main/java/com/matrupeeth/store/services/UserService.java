package com.matrupeeth.store.services;

import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);
    //update
    UserDto updateUser(UserDto userDto,String userId);
    //delete
    void deleteUser(String userId) throws IOException;
    //get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get simle
    UserDto getUserById(String userId);
    //get sigila by email
    UserDto getUserByEmail(String email);
    //other user spedfie
    List<UserDto>searchUser(String keyword);

    Optional<User>findUserByEmailForGoogleAuth(String email);
}
