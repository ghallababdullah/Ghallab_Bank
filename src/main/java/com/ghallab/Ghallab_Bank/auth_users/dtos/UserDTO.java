package com.ghallab.Ghallab_Bank.auth_users.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ghallab.Ghallab_Bank.account.dtos.AccountDTO;
import com.ghallab.Ghallab_Bank.role.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id ;

    private  String firstname ;

    private  String lastname ;

    private String phoneNumber ;


    private String email ;

    @JsonIgnore
    private String password ;

    private String profilePictureURl ;

    private boolean active ;


    private List <Role> roles;

    @JsonManagedReference
    private List <AccountDTO> accounts ;

    private LocalDateTime createdAt;
    private LocalDateTime updatedeAt ;

}
