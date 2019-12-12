package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sso")
public class SsoController {
    @Reference
    IUserService userService;
    /**
     * 前往登录页面
     * @return
     */
    @RequestMapping("/tologin")
    public String tologin(){
    return "login";
}

/**
 * 前往注册页面
 */
@RequestMapping("/toregister")
public String toregister(){
    return "register";
}

/**
 * 注册用户
 */
@RequestMapping("register")
    public String regist(User user, Model model){
    int result = userService.register(user);
    if(result == -1){
        model.addAttribute("msg","用户名已存在");
        return "register";
    }else if(result == -2){
        model.addAttribute("msg","邮箱已经被注册");
        return "register";
    }
    return "login";
}
}
