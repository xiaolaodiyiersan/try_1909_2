package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Email;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/forget")
public class ForgetController {

    @Reference
    IUserService userService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;


    /**
     * 进入忘记密码页面
     *
     * @return
     */
    @RequestMapping("/toforget")
    public String toforget() {
        return "forget";
    }

    /**
     * 发送邮件
     */
    @RequestMapping("/sendEmail")
    @ResponseBody
    public ResultData<Map<String, String>> sendEmail(String username) {

        User user = userService.queryUserByUsername(username);
        if (user == null) {
            return new ResultData<Map<String, String>>().setCode(ResultData.errorList.ERROR);
        }

        //创建一个邮件
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,username);
        redisTemplate.expire("uuid",5, TimeUnit.MINUTES);
        String url = "http://localhost:8082/forget/tosetPass?token="+token;
        Email email = new Email()
                .setTo(user.getEmail())
                .setContext("点击<a href='" + url + "'>这里</a>找回密码")
                .setSendTime(new Date())
                .setSubject("找回密码");
        rabbitTemplate.convertAndSend("mail_exchange", "", email);




        String showEmail = user.getEmail().replace(user.getEmail().substring(3, user.getEmail().lastIndexOf("@")), "xxxxxx");
        String toEmail = user.getEmail().substring(user.getEmail().lastIndexOf("@") + 1);
        Map<String, String> map = new HashMap<>();
        map.put("showEmail", showEmail);
        map.put("toEmail", toEmail);
        return new ResultData<Map<String, String>>().setCode(ResultData.errorList.OK).setMsg("邮箱发送成功").setData(map);
    }
@RequestMapping("/tosetPass")
    public String tosetPass(String token, Model model){
        model.addAttribute("token",token);
return "tosetPass";
}

@RequestMapping("/setPass")
    public String setPass(String token,String password){
    String username = redisTemplate.opsForValue().get(token);
    if(username == null){
       return "setError";
    }
        userService.setPass(username,password);
        redisTemplate.delete(token);
        return "tosetPass";
}
}
