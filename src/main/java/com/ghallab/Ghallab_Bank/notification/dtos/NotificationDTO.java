package com.ghallab.Ghallab_Bank.notification.dtos;

import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class NotificationDTO {

    private Long id ;

    private String subject ;
    @NotBlank(message = "Recipient is required")
    private  String recipient ;

    private String body ;



    private NotificationType notificationType;


    //For values/variables to be passed into email templates
    private String templateName ;

    private Map<String , Object> templateVariable ;
}
