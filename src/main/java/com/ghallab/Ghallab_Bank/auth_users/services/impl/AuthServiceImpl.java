package com.ghallab.Ghallab_Bank.auth_users.services.impl;

import com.ghallab.Ghallab_Bank.account.entity.Account;
import com.ghallab.Ghallab_Bank.account.services.AccountService;
import com.ghallab.Ghallab_Bank.auth_users.dtos.LoginRequest;
import com.ghallab.Ghallab_Bank.auth_users.dtos.LoginResponse;
import com.ghallab.Ghallab_Bank.auth_users.dtos.RegistrationRequest;
import com.ghallab.Ghallab_Bank.auth_users.dtos.ResetPasswordRequest;
import com.ghallab.Ghallab_Bank.auth_users.entity.PasswordResetCode;
import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.auth_users.repo.PasswordResetCodeRepo;
import com.ghallab.Ghallab_Bank.auth_users.repo.UserRepo;
import com.ghallab.Ghallab_Bank.auth_users.services.AuthService;
import com.ghallab.Ghallab_Bank.auth_users.services.CodeGenerator;
import com.ghallab.Ghallab_Bank.enums.AccountType;
import com.ghallab.Ghallab_Bank.enums.Currency;
import com.ghallab.Ghallab_Bank.exceptions.BadRequestException;
import com.ghallab.Ghallab_Bank.exceptions.NotFoundException;
import com.ghallab.Ghallab_Bank.notification.dtos.NotificationDTO;
import com.ghallab.Ghallab_Bank.notification.services.NotificationService;
import com.ghallab.Ghallab_Bank.res.Response;
import com.ghallab.Ghallab_Bank.role.entity.Role;
import com.ghallab.Ghallab_Bank.role.repo.RoleRepo;
import com.ghallab.Ghallab_Bank.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {

    private final UserRepo userRepo ;
    private final RoleRepo roleRepo ;
    private final PasswordEncoder passwordEncoder ;
    private final TokenService tokenService ;
    private final NotificationService notificationService ;
    private final CodeGenerator codeGenerator ;
    private final AccountService accountService ;

    private final PasswordResetCodeRepo passwordResetCodeRepo ;
    @Value("${password.reset.link}")
    private String resetLink ;

    @Override
    public Response<String> register(RegistrationRequest request) {
        List<Role> roles ;

        if (request.getRoles() == null || request.getRoles().isEmpty()){
            // defaulte to customer
            Role defaultRole = roleRepo.findByName("CUSTOMER").orElseThrow(()-> new NotFoundException("Customer role not found")) ;
            roles = Collections.singletonList(defaultRole);
        }else {
            roles = request.getRoles().stream()
                    .map(roleName ->roleRepo.findByName(roleName)
                            .orElseThrow(()->new NotFoundException("Role not Found" + roleName)))
                    .toList() ;

        }
        if (userRepo.findByEmail(request.getEmail()).isPresent()){
            throw  new BadRequestException("Email already present") ;


        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .active(true)
                .build();
        User savedUser = userRepo.save(user);

        //creaateACCOUNT NUMBER FOR THE USER
        Account savedAccount = accountService.createAccount(AccountType.SAVING , savedUser);

        // send a welcome email
        Map<String , Object>  vars = new HashMap<>() ;
        vars.put("name" , savedUser.getFirstName()) ;
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome To Ghallab Bank \uD83D\uDE4C")
                .templateName("welcome")
                .templateVariable(vars)
                .build();
         notificationService.sendEmail(notificationDTO,savedUser);


        // TODO send account creation and credentials
        Map<String , Object>  accountVars = new HashMap<>() ;
        accountVars.put("name" , savedUser.getFirstName()) ;
        accountVars.put("accountNumber", savedAccount.getAccountNumber());
        accountVars.put("accountType",AccountType.SAVING.name()) ;
        accountVars.put("currency", Currency.USD) ;

        NotificationDTO accountCreationEmail = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Your New Ghallab Bank Account has been Created !")
                .templateName("account-created")
                .templateVariable(accountVars)
                .build();
        notificationService.sendEmail(accountCreationEmail,savedUser);

        return Response.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("account has been created succesfully")
               .data("Email of your account details has been sent to"+savedUser.getEmail() + "Your account number is" +savedAccount.getAccountNumber())
                .build();

    }

    @Override
    public Response<LoginResponse> Login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail() ;
        String password = loginRequest.getPassword();
        User user = userRepo.findByEmail(email).orElseThrow(()->new NotFoundException("The email is not found "));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Password doesn't match");
        }

        String token = tokenService.generateToken(user.getEmail());

        LoginResponse loginResponse = LoginResponse.builder()
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .token(token)
                .build();

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successful")
                .data(loginResponse)
                .build();



    }
    @Override
    @Transactional
    public Response<?> forgetPassword(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(()-> new NotFoundException("User not Found")) ;
        passwordResetCodeRepo.deleteByUserId(user.getId());

        String code = codeGenerator.generateUniqueCode() ;

        PasswordResetCode resetCode = PasswordResetCode.builder()
                .user(user)
                .code(code)
                .expireyDate(calculateExpirationDate())
                .used(false).
                build();


        passwordResetCodeRepo.save(resetCode) ;
        //send email to reset the password
        Map<String , Object>  vars = new HashMap<>() ;
        vars.put("name" , user.getFirstName()) ;
        vars.put("resetLink" , resetLink + code) ;

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Password Reset Code")
                .templateName("password-reset")
                .templateVariable(vars)
                .build();
        notificationService.sendEmail(notificationDTO,user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password reset code sent to your email")
                .build() ;



    }



    @Override
    @Transactional
    public Response<?> updatePasswordViaRequest(ResetPasswordRequest resetPasswordRequest) {
        String code = resetPasswordRequest.getCode() ;
        String newPassword= resetPasswordRequest.getNewPassword();

        //find and validate code
        PasswordResetCode resetCode = passwordResetCodeRepo.findByCode(code)
                .orElseThrow(()->new BadRequestException("Invalid reset code")) ;

        //check expiration first
        if (resetCode.getExpireyDate().isBefore(LocalDateTime.now())){
            passwordResetCodeRepo.delete(resetCode);
            throw  new BadRequestException("Reset code has expired !") ;
        }

        User user = resetCode.getUser() ;
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        passwordResetCodeRepo.delete(resetCode);

        Map<String , Object>  templateVariables = new HashMap<>() ;
        templateVariables.put("name" , user.getFirstName()) ;

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Password was updated")
                .templateName("password-update-confirmation")
                .templateVariable(templateVariables)
                .build();
        notificationService.sendEmail(notificationDTO,user);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password updated succesfully").build();



    }

    private LocalDateTime calculateExpirationDate(){

        return LocalDateTime.now().plusHours(1);
    }
}
