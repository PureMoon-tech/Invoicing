package com.example.invc_proj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Login  {


    @GetMapping("/")
    public String greet()
    {
        return "Welcome to Invoicing Application";
    }



}
