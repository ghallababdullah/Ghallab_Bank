package com.ghallab.Ghallab_Bank.auth_users.services;

import com.ghallab.Ghallab_Bank.auth_users.dtos.LoginRequest;
import com.ghallab.Ghallab_Bank.auth_users.dtos.LoginResponse;
import com.ghallab.Ghallab_Bank.auth_users.dtos.RegistrationRequest;
import com.ghallab.Ghallab_Bank.auth_users.dtos.ResetPasswordRequest;
import com.ghallab.Ghallab_Bank.res.Response;

public interface AuthService {
    Response<String> register(RegistrationRequest request) ;
    Response<LoginResponse> Login(LoginRequest loginRequest) ;
    Response<?> forgetPassword(String email) ;
    Response<?> updatePasswordViaRequest(ResetPasswordRequest resetPasswordRequest) ;
}
