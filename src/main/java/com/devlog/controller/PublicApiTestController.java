package com.devlog.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@CrossOrigin
public class PublicApiTestController {


    @GetMapping("/all")
    public String allTest(){
        return "API Test 1";
    }

    // Available to all authenticated users
    @GetMapping("/admin")
    public String test1(){
        return "API Test 1";
    }

    // Available to managers
    @GetMapping("/manager")
    public String reports(){
        return "API Test 2";
    }
}
