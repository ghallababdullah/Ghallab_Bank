package com.ghallab.Ghallab_Bank.audit_dashboard.services;

import com.ghallab.Ghallab_Bank.account.dtos.AccountDTO;
import com.ghallab.Ghallab_Bank.auth_users.dtos.UserDTO;
import com.ghallab.Ghallab_Bank.transaction.dtos.TransactionDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {
    Map<String, Long> getSystemTotals();

    Optional<UserDTO> findUserByEmail(String email);

    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);

    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);

    Optional<TransactionDTO> findTransactionById(Long transactionId);
}
