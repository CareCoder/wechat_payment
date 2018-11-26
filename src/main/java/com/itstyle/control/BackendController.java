package com.itstyle.control;

import com.itstyle.common.YstCommon;
import com.itstyle.dao.RedisDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.UUID;

@RequestMapping("/backend")
@Controller
public class BackendController {
    @Resource
    private RedisDao redisDao;
    @RequestMapping("/login.html")
    public String login(Model model) {
        String uuid = UUID.randomUUID().toString();
        model.addAttribute("uuid", uuid);
        model.addAttribute("appid", YstCommon.APPID);
        redisDao.hset(YstCommon.USER_SCAN_CODE_LOGIN, uuid, "");
        return "/backend/login";
    }

    @RequestMapping("/index.html")
    public String index() {
        System.out.println("登陆成功。。。。");
        return "/backend/index";
    }
}
