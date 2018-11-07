package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;

public interface AccountService {

    PageResponse<Account> getAll(int page, int size);

    Account insert(Account account);

    RequestAccount edit(RequestAccount account);

    void delete(Long id);

    Account login(String account, String password);
}
