package com.ghallab.Ghallab_Bank.transaction.repo;

import com.ghallab.Ghallab_Bank.account.entity.Account;
import com.ghallab.Ghallab_Bank.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo  extends JpaRepository<Transaction , Long> {

    Page<Transaction> findByAccount_AccountNumber (String accountNumebr , Pageable pageable) ;

    List<Transaction> findByAccount_AccountNumber (String accountNumebr ) ;

}
