package com.matrupeeth.store.services.Impl;

import com.matrupeeth.store.controller.UserController;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.entities.User;
import com.matrupeeth.store.exception.ResourcesNotFoundException;
import com.matrupeeth.store.helper.Helper;
import com.matrupeeth.store.repositories.UserRepository;
import com.matrupeeth.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private  String  imagePath;
    @Override
    public UserDto createUser(UserDto userDto) {
        //gernate uniqu id
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //set password endocde
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user =dtoToEntity(userDto);
        User user1 = this.userRepository.save(user);
        UserDto userDto1=entityTodtos(user1);
        return userDto1;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("user not foun dwith giben id "));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        User saveUser = this.userRepository.save(user);
        UserDto updateDto = entityTodtos(saveUser);
        return updateDto;
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("user not foun dwith giben id "));

        // delete image
        String fullPath = imagePath + user.getImageName();

       try {
           Path path= Paths.get(fullPath);
           Files.delete(path);

       }catch (Exception e)
       {
           logger.info("image file not found");
           e.printStackTrace();
       }

        this.userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;
        Pageable pageable   = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = this.userRepository.findAll(pageable);
//        List<User> Allusers = page.getContent();
//        List<UserDto> dtoList = Allusers.stream().map(user -> entityTodtos(user)).collect(Collectors.toList());
//        PageableResponse response=new PageableResponse();
//        response.setContent(dtoList);
//        response.setPageNumber(page.getNumber());
//        response.setPageSize(page.getSize());
//        response.setTotalElements(page.getTotalElements());
//        response.setTotalPages(page.getTotalPages());
//        response.setLastPage(page.isLast());

        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("user not foun dwith giben id "));
        UserDto userDtodata = entityTodtos(user);
        return userDtodata;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResourcesNotFoundException("user not with geibng emila id "));
        UserDto userDtoByEmail = entityTodtos(user);
        return userDtoByEmail;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = this.userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityTodtos(user)).collect(Collectors.toList());
        return dtoList;
    }

    private UserDto entityTodtos(User user1) {
//        UserDto userDto = UserDto.builder().userId(user1.getUserId())
//                .name(user1.getName())
//                .email(user1.getEmail())
//                .password(user1.getPassword())
//                .about(user1.getAbout())
//                .gender(user1.getGender())
//                .imageName(user1.getImageName())
//                .build();
        return mapper.map(user1,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder().userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();
        return  mapper.map(userDto,User.class);
    }

}
