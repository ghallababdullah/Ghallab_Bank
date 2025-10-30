package com.ghallab.Ghallab_Bank.auth_users.repo;

import com.ghallab.Ghallab_Bank.auth_users.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetCodeRepo extends  JpaRepository<PasswordResetCode, Long > {
    Optional<PasswordResetCode> findByCode(String Code);

    void deleteByUserId(Long Id) ;
}
