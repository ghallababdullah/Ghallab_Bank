package com.ghallab.Ghallab_Bank.auth_users.dtos;

import jakarta.validation.constraints.NotBlank;

public class UpdatePasswordRequest {

    @NotBlank(message = "Old Password is required")
    private String oldPassword ;

    @NotBlank(message = "New Password is required")
    private String newPassword ;
}
