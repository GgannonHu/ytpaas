package com.winton.ytpaas.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class SystemController {

    @RequestMapping("/index")
    public String index() {
        return "/system/index";
    }

    @RequestMapping("/user")
    public String user(){
        return "/system/user/list";
    }
    @RequestMapping("/menu")
    public String menu(){
        return "/system/menu/list";
    }
    @RequestMapping("/icon")
    public String icon() {
        return "/system/icon/list";
    }

    @RequestMapping("/user/edit")
    public String userEdit(){
        return "/system/user/edit";
    }

    @RequestMapping("/jg")
    public String jg(){
        return "/system/jg/list";
    }

    @RequestMapping("/jg/edit")
    public String jgEdit(){
        return "/system/jg/edit";
    }
    @RequestMapping("/role")
    public String role(){
        return "/system/role/list";
    }

    @RequestMapping("/role/edit")
    public String roleEdit(){
        return "/system/role/edit";
    }
    @RequestMapping("/role/gnqx")
    public String roleGNQX(){
        return "/system/role/gnqx";
    }
    @RequestMapping("/role/sjqx")
    public String roleSJQX(){
        return "/system/role/sjqx";
    }
    @RequestMapping("/jg/select")
    public String jgSelect(){
        return "/system/jg/select";
    }
}