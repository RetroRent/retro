package com.retro_rent.managerApplication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class MainController {
    @GetMapping("/RetroRent/hello")
    public String hello() {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }
}
