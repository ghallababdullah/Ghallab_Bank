package com.ghallab.Ghallab_Bank.transaction.services;

import com.ghallab.Ghallab_Bank.res.Response;
import com.ghallab.Ghallab_Bank.transaction.dtos.TransactionDTO;
import com.ghallab.Ghallab_Bank.transaction.dtos.TransactionRequest;

import java.util.List;

public interface TransactionService {
    Response<?> createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDTO>> getTransactionsForMyAccount(String accountNumber, int page, int size);
}
