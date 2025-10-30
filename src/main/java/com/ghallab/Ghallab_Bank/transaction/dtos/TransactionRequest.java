package com.ghallab.Ghallab_Bank.transaction.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ghallab.Ghallab_Bank.account.entity.Account;
import com.ghallab.Ghallab_Bank.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class TransactionRequest {
    private TransactionType transactionType  ;

    private BigDecimal amount ;

    private String accountNumber ;

    private String description ;

    private String destinationAccountNumber ;// for receiving account number if it's a transfer



}
