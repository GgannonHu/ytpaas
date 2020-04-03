package com.winton.ytpaas.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class MainErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500  可定制
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode != null) {
            if(statusCode == 500){
                return "/home/error";
            }else if(statusCode == 404){
                return "/home/error";
            }else if(statusCode == 401){
                return "/home/error";
            }
        }
        return "/home/error";
    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}