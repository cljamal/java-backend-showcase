package com.sultanov.present_project.features.init;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitApp {
    @GetMapping
    public String init() {
        return "Hello World!";
    }
}
