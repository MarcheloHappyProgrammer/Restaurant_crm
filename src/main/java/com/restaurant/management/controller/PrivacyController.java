package com.restaurant.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/privacyPolicy")
public class PrivacyController {
	@GetMapping
    public String privacyPolicy() {
        return "customers/privacy-policy"; 
    }
}
