package com.ghallab.Ghallab_Bank.account.dtos;


import com.fasterxml.jackson.annotation.*;
import com.ghallab.Ghallab_Bank.auth_users.dtos.UserDTO;
import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.AccountStatus;
import com.ghallab.Ghallab_Bank.enums.AccountType;
import com.ghallab.Ghallab_Bank.enums.Currency;
import com.ghallab.Ghallab_Bank.transaction.dtos.TransactionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id ;

    private String accountNumber ;

    private BigDecimal balance ;


    private AccountType accountType ;

    @JsonBackReference
    private UserDTO user ;


    private Currency currency ;


    private AccountStatus accountStatus;

    @JsonManagedReference
    private List<TransactionDTO> transactions ;

    private LocalDateTime closedAt ;

    private LocalDateTime createdAt ;

    private LocalDateTime updatedAt ;
}
