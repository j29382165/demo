package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login"; // 返回 login.html
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // 返回 register.html
    }
}