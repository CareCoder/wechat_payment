package com.itstyle.service;

import com.itstyle.common.PageResponse;
import com.itstyle.domain.account.Account;
import com.itstyle.domain.account.req.RequestAccount;
import com.itstyle.domain.account.resp.ResponseAccount;

public interface AccountService {

    PageResponse<ResponseAccount> list(int page, int limit);

    Account insert(Account account);

    RequestAccount edit(RequestAccount account);

    void delete(Long id);

    Account login(String account, String password);

    Account getById(Long id);
}
