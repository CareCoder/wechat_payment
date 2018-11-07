package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.Role;
import com.itstyle.domain.account.req.RequestAccount;
import com.itstyle.domain.account.resp.ResponseAccount;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.mapper.AccountMapper;
import com.itstyle.mapper.RoleMapper;
import com.itstyle.service.AccountService;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {


    private AccountMapper accountMapper;
    private RoleMapper roleMapper;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper, RoleMapper roleMapper) {
        this.accountMapper = accountMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public PageResponse<ResponseAccount> list(int page, int limit) {
        Page<Account> accounts = accountMapper.findAll(PageResponse.getPageRequest(page, limit));
        List<Account> content = accounts.getContent();
        List<Role> roles = roleMapper.findAll();
        Map<Long, String> mapRole = roles.stream().collect(Collectors.toMap(Role::getId, Role::getName));
        List<ResponseAccount> responseAccounts = content.stream().map(account -> {
            ResponseAccount responseAccount = new ResponseAccount();
            BeanUtilIgnore.copyPropertiesIgnoreNull(account, responseAccount);
            responseAccount.setRoleName(mapRole.get(responseAccount.getId()));
            return responseAccount;
        }).collect(Collectors.toList());
        return new PageResponse<>(accounts.getTotalElements(), responseAccounts);
    }

    @Override
    public Account insert(Account account) {
        String password = account.getPassword();
        try {
            String md5Password = Md5Util.getMD5(password);
            account.setPassword(md5Password);
            account.setCreateTime(new Date());
        } catch (Exception e) {
            log.error("encryption error", e);
            throw new BusinessException("加密出错");
        }
        return accountMapper.save(account);
    }

    @Override
    public RequestAccount edit(RequestAccount account) {
        Account oldAccount = accountMapper.getOne(account.getId());
        AssertUtil.assertNotNull(oldAccount, () -> new BusinessException("修改账户不存在"));
        String oldPassword = account.getOldPassword();
        // 将password加密
        try {
            String md5OldPassword = Md5Util.getMD5(oldPassword);
            if (oldAccount.getPassword().equals(md5OldPassword)) {
                String md5NewPassword = Md5Util.getMD5(account.getNewPassword());
                account.setNewPassword(md5NewPassword);
            } else {
                throw new BusinessException("旧密码错误");
            }
        } catch (Exception e) {
            log.error("encryption error", e);
            throw new BusinessException("加密出错");
        }
        account.setUpdateTime(new Date());
        BeanUtilIgnore.copyPropertiesIgnoreNull(account, oldAccount);
        Account save = accountMapper.save(oldAccount);
        if (save == null) {
            throw new BusinessException("修改失败，请稍后重试");
        }
        return account;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        accountMapper.delete(id);
        log.info("[AccountServiceImp]  delete account id [{}] success [{}]", id);
    }

    @Override
    public Account login(String account, String password) {
        Account oAccount = accountMapper.findByAccount(account);
        AssertUtil.assertNotNull(oAccount, () -> new BusinessException("账号不存在"));
        String pass = oAccount.getPassword();
        try {
            String md5Password = Md5Util.getMD5(password);
            AssertUtil.assertNull(md5Password.equals(pass), () -> new BusinessException("密码不正确"));
        } catch (Exception e) {
            log.error("encryption error", e);
            throw new BusinessException("加密出错");
        }
        return oAccount;
    }


}
