package com.ghallab.Ghallab_Bank.auth_users.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_code")
public class PasswordResetCode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;


    @Column(unique = true)
    private String code  ;

    @OneToOne(targetEntity = User.class , fetch = FetchType.EAGER)
    @JoinColumn(nullable = false , name = "user_id")
    private User user ;

    private LocalDateTime expireyDate ;


    private boolean used = false ;
}