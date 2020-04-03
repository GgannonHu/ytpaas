package com.winton.ytpaas.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    
    @RequestMapping("/")
    public String login() {
        return "/home/login";
    }
    
    @RequestMapping("/index")
    public String index() {
        return "/home/index";
    }
    
}