package com.itstyle.control;

import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.domain.vo.LoginRequestVo;
import com.itstyle.domain.vo.LoginResponseVo;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccountService;
import com.itstyle.utils.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/external")
public class ExternalInterfaceController {

    private AccountService accountService;

    @Autowired
    public ExternalInterfaceController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/login")
    @ResponseBody
    public LoginResponseVo login(LoginRequestVo loginRequestVo) {
        LoginResponseVo responseVo = new LoginResponseVo();
        if (loginRequestVo == null) {
            responseVo.setErrorCode(Status.ERROR);
            responseVo.setErrorDesc("参数为空");
            return responseVo;
        }
        responseVo.setServiceCode(loginRequestVo.getServiceCode());
        try {
            accountService.login(loginRequestVo.getUserName(), loginRequestVo.getPassword());
        } catch (BusinessException e) {
            String message = e.getMessage();
            responseVo.setErrorCode(Status.ERROR);
            responseVo.setErrorDesc(message);
        }

        return responseVo;
    }
}
