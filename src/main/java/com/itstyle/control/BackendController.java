package com.itstyle.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/backend")
@Controller
public class BackendController {
    @RequestMapping("/login.html")
    public String login() {
        return "/backend/login";
    }

    @RequestMapping("/index.html")
    public String index() {
        System.out.println("登陆成功。。。。");
        return "/backend/index";
    }
}
