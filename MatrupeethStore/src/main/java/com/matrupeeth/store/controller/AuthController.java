package com.matrupeeth.store.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.matrupeeth.store.dtos.JwtRequest;
import com.matrupeeth.store.dtos.JwtResponse;
import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.entities.User;
import com.matrupeeth.store.exception.BadApiRequest;
import com.matrupeeth.store.security.JwtHelper;
import com.matrupeeth.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private Logger logger= LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;

    @Value("${googleClientId}")
    private  String googleClinetId;
    @Value("${newPassword}")
    private  String newPassword;


    @PostMapping("/login")
    public  ResponseEntity<JwtResponse>login(@RequestBody JwtRequest jwtRequest)
    {
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtHelper.generateToken(userDetails);
        UserDto userDto=modelMapper.map(userDetails,UserDto.class);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .user(userDto).build();
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e) {
            throw new BadApiRequest("Couldn't authenticate user invalid user  ane password exception");
        }

    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String name = principal.getName();
        return  new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name),UserDto.class), HttpStatus.OK);
    }

    //login with google account
    @PostMapping("/google")
    public ResponseEntity<JwtResponse>loginWithGoogleAccount(@RequestBody Map<String,String> data) throws IOException {
        //get the id token from the token
        String idToken=data.get("idToken").toString();
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifire = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClinetId));
        GoogleIdToken googleIdToken= GoogleIdToken.parse(verifire.getJsonFactory(),idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        logger.info("payload: {} " + payload);
        String email = payload.getEmail();
        User user =null;
         user = userService.findUserByEmailForGoogleAuth(email).orElseThrow(null);
         if(user==null)
         {
           user=  this.saveUser(email,data.get("name"),data.get("photoUrl").toString());
         }

        ResponseEntity<JwtResponse> jwtResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
        return jwtResponseEntity;
    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDto newUser = UserDto.builder()
                .name(name)
                .imageName(photoUrl)
                .email(email)
                .password(newPassword)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);
        return  modelMapper.map(user,User.class);

    }

}
