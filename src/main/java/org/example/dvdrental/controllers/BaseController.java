package org.example.dvdrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BaseController {
    @GetMapping()
    public String showHomePage() {
        return "home";
    }
}
