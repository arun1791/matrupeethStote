package com.matrupeeth.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("/")
    public  String testing()
    {
        return "Welcome to matrupeth store";
    }
}
