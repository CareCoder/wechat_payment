package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccountService;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;

    @Value("${super-admin.user-id}")
    private Long userId;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    @ResponseBody
    public PageResponse<Account> getAll(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        PageResponse<Account> pageResponse = accountService.getAll(page, size);
        log.info("[AccountController] data is [{}]", pageResponse.getData());
        return pageResponse;
    }

    @PostMapping("/save")
    @ResponseBody
    public Response save(@RequestBody Account account) {
        AssertUtil.assertNotNull(account, () -> new BusinessException("account is null"));
        AssertUtil.assertNotEmpty(account.getAccount(), () -> new BusinessException("账号不能为空"));
        AssertUtil.assertNotEmpty(account.getPassword(), () -> new BusinessException("密码不能为空"));
        AssertUtil.assertNotNull(account.getRoleId(), () -> new BusinessException("类型不能为空"));
        Account insert = accountService.insert(account);
        log.info("[AccountController] save success [{}]", insert);
        return Response.build(Status.NORMAL, null, null);
    }

    @PostMapping("/edit")
    @ResponseBody
    public Response edit(@RequestBody RequestAccount requestAccount) {
        AssertUtil.assertNotNull(requestAccount, () -> new BusinessException("request account is null"));
        AssertUtil.assertNotNull(requestAccount.getOldPassword(), () -> new BusinessException("旧密码不能为空"));
        AssertUtil.assertNotNull(requestAccount.getNewPassword(), () -> new BusinessException("新密码不能为空"));
        AssertUtil.assertNotNull(requestAccount.getRoleId(), () -> new BusinessException("类型不能为空"));
        RequestAccount account = accountService.edit(requestAccount);
        log.info("[AccountController] edit success [{}]", account);
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Response delete(@PathVariable("id") Long id) {
        AssertUtil.assertNotNull(id, () -> new BusinessException("删除的id不能为空"));
        AssertUtil.assertFalse(id.equals(userId), () -> new BusinessException("超级管理员不能被删除"));
        accountService.delete(id);
        return Response.build(Status.NORMAL, null, null);
    }

    @PostMapping("/login")
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password, HttpSession session) {
        AssertUtil.assertNotNull(account, () -> new BusinessException("账号不能为空"));
        AssertUtil.assertNotNull(password, () -> new BusinessException("密码不能为空"));
        Account loginAccount = accountService.login(account, password);
        session.setAttribute(YstCommon.LOGIN_ACCOUNT, loginAccount);
        SystemLoggerHelper.log(loginAccount.getUsername(), "登陆", "登陆系统");
        // 登陆成功重定向到首页
        return "redirect:/main.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        if (account != null) {
            session.removeAttribute(YstCommon.LOGIN_ACCOUNT);
            SystemLoggerHelper.log(account.getUsername(), "登出", "登出系统");
        }
        return "redirect:/login.html";
    }

}
