package com.itstyle.control;

import com.itstyle.common.PageResponse;
import com.itstyle.common.SystemLoggerHelper;
import com.itstyle.common.YstCommon;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;
import com.itstyle.domain.account.resp.ResponseAccount;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccountService;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/list")
    @ResponseBody
    public PageResponse<ResponseAccount> list(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        PageResponse<ResponseAccount> pageResponse = accountService.list(page, limit);
        log.info("[AccountController] data is [{}]", pageResponse.getData());
        return pageResponse;
    }

    @PostMapping("/save")
    @ResponseBody
    public Response save(Account account) {
        AssertUtil.assertNotNull(account, () -> new BusinessException("account is null"));
        AssertUtil.assertNotEmpty(account.getUsername(), () -> new BusinessException("用户名不能为空"));
        AssertUtil.assertNotEmpty(account.getTAccount(), () -> new BusinessException("账号不能为空"));
        AssertUtil.assertNotEmpty(account.getPassword(), () -> new BusinessException("密码不能为空"));
        AssertUtil.assertNotNull(account.getRoleId(), () -> new BusinessException("类型不能为空"));
        Account insert = accountService.insert(account);
        log.info("[AccountController] save success [{}]", insert);
        SystemLoggerHelper.log("保存", "新增管理员");
        return Response.build(Status.NORMAL, null, null);
    }

    @PostMapping("/edit")
    @ResponseBody
    public Response edit(RequestAccount requestAccount) {
        AssertUtil.assertNotNull(requestAccount, () -> new BusinessException("request account is null"));
//        AssertUtil.assertNotNull(requestAccount.getOldPassword(), () -> new BusinessException("旧密码不能为空"));
//        AssertUtil.assertNotNull(requestAccount.getNewPassword(), () -> new BusinessException("新密码不能为空"));
        AssertUtil.assertNotNull(requestAccount.getRoleId(), () -> new BusinessException("类型不能为空"));
        RequestAccount account = accountService.edit(requestAccount);
        log.info("[AccountController] edit success [{}]", account);
        SystemLoggerHelper.log("修改", "修改管理员密码:");
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Response delete(@PathVariable("id") Long id) {
        AssertUtil.assertNotNull(id, () -> new BusinessException("删除的id不能为空"));
        AssertUtil.assertFalse(id.equals(userId), () -> new BusinessException("超级管理员不能被删除"));
        accountService.delete(id);
        SystemLoggerHelper.log("删除", "删除管理员");
        return Response.build(Status.NORMAL, null, null);
    }

    @PostMapping("/login")
    @ResponseBody
    public Response login(@RequestParam("account") String account,
                        @RequestParam("password") String password, HttpSession session) {
        AssertUtil.assertNotNull(account, () -> new BusinessException("账号不能为空"));
        AssertUtil.assertNotNull(password, () -> new BusinessException("密码不能为空"));
        Account loginAccount = accountService.login(account, password);
        session.setAttribute(YstCommon.LOGIN_ACCOUNT, loginAccount);
        SystemLoggerHelper.log(loginAccount.getUsername(), loginAccount.getRoleId(), "登陆", "登陆系统");
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/logout")
    @ResponseBody
    public Response logout(HttpSession session) {
        Account account = (Account) session.getAttribute(YstCommon.LOGIN_ACCOUNT);
        if (account != null) {
            session.removeAttribute(YstCommon.LOGIN_ACCOUNT);
            SystemLoggerHelper.log(account.getUsername(), account.getRoleId(), "登出", "登出系统");
        }
        return Response.build(Status.NORMAL, null, null);
    }

    @GetMapping("/info")
    public String info() {
        return "/backend/account-info";
    }

    @GetMapping("/account-add.html")
    public String accountAddUI() {
        return "/backend/account-add";
    }

    @GetMapping("/account-edit.html")
    public String accountEditUI(Long id, Model model) {
        Account account = accountService.getById(id);
        model.addAttribute("account_info", account);
        return "/backend/account-edit";
    }

}
