package com.ghallab.Ghallab_Bank.account.entity;
import com.ghallab.Ghallab_Bank.transaction.entity.Transaction;
import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.AccountStatus;
import com.ghallab.Ghallab_Bank.enums.AccountType;
import com.ghallab.Ghallab_Bank.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.engine.internal.Cascade;
import org.springframework.beans.propertyeditors.CurrencyEditor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false , unique = true , length = 15)
    private String accountNumber ;
    @Column(nullable = false , precision = 19, scale = 2)
    private BigDecimal balance= BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private AccountType accountType ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user ;

    @Enumerated(EnumType.STRING)
    private Currency currency ;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "account" , cascade = CascadeType.ALL , orphanRemoval = true , fetch =FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>() ;

    private LocalDateTime closedAt ;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt ;


}
