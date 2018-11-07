package com.itstyle.service.impl;

import com.itstyle.common.PageResponse;
import com.itstyle.common.Pagination;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;
import com.itstyle.exception.AssertUtil;
import com.itstyle.exception.BusinessException;
import com.itstyle.mapper.AccountMapper;
import com.itstyle.service.AccountService;
import com.itstyle.utils.BeanUtilIgnore;
import com.itstyle.utils.Md5Util;
import com.itstyle.utils.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.OptionalDouble;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {


    private AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public PageResponse<Account> getAll(int page, int size) {
        Pagination<Account> pagination = new Pagination<>();
        return pagination.execute(page, size, Status.NORMAL, null, () -> accountMapper.findAll());
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
//        if (i == null || i != 1) {
//            throw new BusinessException("删除失败，请稍后重试");
//        }
        log.info("[AccountServiceImp]  delete account id [{}] success [{}]", id);
    }
}
