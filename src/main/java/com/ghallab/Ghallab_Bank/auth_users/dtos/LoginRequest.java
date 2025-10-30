package com.ghallab.Ghallab_Bank.auth_users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class LoginRequest {

    private String email ;
    private String password ;
}
