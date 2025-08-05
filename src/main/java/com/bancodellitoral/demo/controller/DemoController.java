package com.bancodellitoral.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/test")
public class DemoController {
    @GetMapping
    public String index() {
        return "Hello World";
    }
}
