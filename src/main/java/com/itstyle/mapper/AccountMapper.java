package com.itstyle.mapper;

import com.itstyle.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountMapper extends JpaRepository<Account, Long> {

    Account findByTAccount(String tAccount);
}
