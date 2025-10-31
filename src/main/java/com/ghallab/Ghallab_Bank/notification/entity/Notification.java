package com.ghallab.Ghallab_Bank.notification.entity;

import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.AccountType;
import com.ghallab.Ghallab_Bank.enums.Currency;
import com.ghallab.Ghallab_Bank.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String subject ;
    private  String recipient ;
    private String body ;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user ;

    private LocalDateTime createAt = LocalDateTime.now();
}
