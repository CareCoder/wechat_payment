package com.itstyle.control;

import com.google.gson.Gson;
import com.itstyle.common.DataPage;
import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;
import com.itstyle.domain.park.resp.Response;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.service.AccountService;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;
    private Gson gson;

    @Autowired
    public AccountController(AccountService accountService, Gson gson) {
        this.accountService = accountService;
        this.gson = gson;
    }

    @GetMapping("/all")
    public String getAll(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "size", required = false, defaultValue = "20") int size, Model model) {
        PageResponse<Account> pageResponse = accountService.getAll(page, size);
        log.info("[AccountController] data is [{}]", pageResponse.getData());
        model.addAttribute("account", pageResponse);
        return "account";
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(@RequestBody Account account) {
        AssertUtil.assertNotNull(account, () -> new BusinessException("account is null"));
        AssertUtil.assertNotEmpty(account.getAccount(), () -> new BusinessException("账号不能为空"));
        AssertUtil.assertNotEmpty(account.getPassword(), () -> new BusinessException("密码不能为空"));
        AssertUtil.assertNotNull(account.getRoleId(), () -> new BusinessException("类型不能为空"));
        Account insert = accountService.insert(account);
        log.info("[AccountController] save success [{}]", insert);
        Response response = Response.build(Status.NORMAL, null, null);
        return gson.toJson(response);
    }

    @PostMapping("/edit")
    @ResponseBody
    public String edit(@RequestBody RequestAccount requestAccount) {
        AssertUtil.assertNotNull(requestAccount, () -> new BusinessException("request account is null"));
        AssertUtil.assertNotNull(requestAccount.getOldPassword(), () -> new BusinessException("旧密码不能为空"));
        AssertUtil.assertNotNull(requestAccount.getNewPassword(), () -> new BusinessException("新密码不能为空"));
        AssertUtil.assertNotNull(requestAccount.getRoleId(), () -> new BusinessException("类型不能为空"));
        RequestAccount account = accountService.edit(requestAccount);
        log.info("[AccountController] edit success [{}]", account);
        Response response = Response.build(Status.NORMAL, null, null);
        return gson.toJson(response);
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        AssertUtil.assertNotNull(id, () -> new BusinessException("删除的id不能为空"));
        accountService.delete(id);
        Response response = Response.build(Status.NORMAL, null, null);
        return gson.toJson(response);
    }

}
