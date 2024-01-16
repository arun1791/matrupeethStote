package com.matrupeeth.store.controller;

import com.matrupeeth.store.dtos.ApiResponseMassege;
import com.matrupeeth.store.dtos.ImgaeResponse;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.services.FileService;
import com.matrupeeth.store.services.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(value = "UsersController", description="Rest Api for user  management creating")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imgaeUploadPath;

    private Logger logger= LoggerFactory.getLogger(UserController.class);
    //create
    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = this.userService.createUser(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser( @PathVariable String userId,@Valid @RequestBody UserDto userDto) {
        UserDto userDto1 = this.userService.updateUser(userDto,userId);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMassege> deleteUser(@PathVariable String userId) throws IOException {
        this.userService.deleteUser(userId);
        ApiResponseMassege message = ApiResponseMassege.builder().message("Delete user").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    //get all
    @GetMapping("/")
    public ResponseEntity<PageableResponse<UserDto>>getAllUsers(
            @RequestParam( value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ) {
        PageableResponse<UserDto> allUsers = this.userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
    //get by single
    @GetMapping("/{userId}")
    public  ResponseEntity<UserDto>getByuserId(@PathVariable  String userId)
    {
        return  new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }
    //get by email
    @GetMapping("/email/{email}")
    public  ResponseEntity<UserDto>getByuserEmail(@PathVariable  String email)
    {
        return  new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }
    //get by kyewbord
    @GetMapping("/search/{keyword}")
    public  ResponseEntity<List<UserDto>>getByuserkeywords(@PathVariable  String keyword)
    {
        return  new ResponseEntity<>(userService.searchUser(keyword),HttpStatus.OK);
    }
    //upload user image
    @PostMapping("/image/{userId}")
    public  ResponseEntity<ImgaeResponse> uploadUserImage(
            @PathVariable String userId,
            @RequestParam("userImage")MultipartFile image
            ) throws IOException {
        String imageName = fileService.uploadImage(image, imgaeUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);
        UserDto userDto = userService.updateUser(user, userId);
        ImgaeResponse imgaeResponse=ImgaeResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return  new ResponseEntity<>(imgaeResponse,HttpStatus.CREATED);
    }

    //serve user image

    @GetMapping("/image/{userId}")
    public  void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("user details user :{}",user.getImageName());
        InputStream resource = fileService.getResource(imgaeUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }
}
