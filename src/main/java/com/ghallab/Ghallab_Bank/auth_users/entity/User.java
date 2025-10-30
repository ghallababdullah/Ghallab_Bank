package com.ghallab.Ghallab_Bank.auth_users.entity;

import com.ghallab.Ghallab_Bank.account.entity.Account;
import com.ghallab.Ghallab_Bank.enums.AccountStatus;
import com.ghallab.Ghallab_Bank.role.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@Table(name= "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private  String name ;

    private  String lastname ;

    private String phoneNumber ;

    @Email
    @Column(unique = true , nullable = false)
    @NotBlank(message="email is required")
    private String email ;


    private String password ;

    private String profilePictureURl ;

    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
                name = "users_roles" ,
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List <Role> roles;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
    private List <Account> accounts ;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedeAt ;

}
