package com.ghallab.Ghallab_Bank.transaction.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ghallab.Ghallab_Bank.account.dtos.AccountDTO;
import com.ghallab.Ghallab_Bank.account.entity.Account;
import com.ghallab.Ghallab_Bank.enums.TransactionStatus;
import com.ghallab.Ghallab_Bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    private Long id ;


    private BigDecimal amount;


    private TransactionType transactionType ;

    private LocalDateTime transactionDate ;

    private String description ;


    private TransactionStatus transactionStatus;

    @JsonBackReference
    private AccountDTO account ;


    // for transfer
    private String sourceAccount ;
    private String destinationAccount  ;



}
