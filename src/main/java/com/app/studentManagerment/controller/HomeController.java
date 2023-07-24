package com.app.studentManagerment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @PostMapping("/home")
    public String showHome(@RequestParam("accessToken") String accessToken) {

        return "home";
    }
}
