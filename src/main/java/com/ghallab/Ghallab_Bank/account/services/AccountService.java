package com.ghallab.Ghallab_Bank.account.services;

import com.ghallab.Ghallab_Bank.account.dtos.AccountDTO;
import com.ghallab.Ghallab_Bank.account.entity.Account;
import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.AccountType;
import com.ghallab.Ghallab_Bank.res.Response;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountType accountType, User user);

    Response<List<AccountDTO>> getMyAccounts();

    Response<?> closeAccount(String accountNumber);
}
