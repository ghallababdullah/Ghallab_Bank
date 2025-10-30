package com.ghallab.Ghallab_Bank.auth_users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {

    private String email ;

    private String code ;

    private String newPassword ;

}
