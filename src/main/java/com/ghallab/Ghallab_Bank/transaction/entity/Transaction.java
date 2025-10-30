package com.ghallab.Ghallab_Bank.transaction.entity;

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

@Entity
@Data
@Builder
@Table(name= "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    private TransactionType transactionType ;

    @Column(nullable = false )
    private LocalDateTime transactionDate = LocalDateTime.now();

    private String description ;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private  Account account ;


    // for transfer
    private String sourceAccount ;
    private String destinationAccount  ;





;

}
