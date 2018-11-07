package com.itstyle.common;

import com.itstyle.domain.account.Account;

public class CustomContext {
    private static final ThreadLocal<Account> ACCOUNT_LOCAL = new ThreadLocal<>();

    public static void setAccount(Account account) {
        ACCOUNT_LOCAL.set(account);
    }

    public static Account getAccount() {
        return ACCOUNT_LOCAL.get();
    }

    public static void remove() {
        ACCOUNT_LOCAL.remove();
    }
}
