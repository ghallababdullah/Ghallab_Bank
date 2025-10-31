package com.ghallab.Ghallab_Bank.notification.services;

import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.NotificationType;
import com.ghallab.Ghallab_Bank.notification.dtos.NotificationDTO;
import com.ghallab.Ghallab_Bank.notification.entity.Notification;
import com.ghallab.Ghallab_Bank.notification.repo.NotificationRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepo notificationRepo ;
    private final JavaMailSender mailSender ;
    private final TemplateEngine templateEngine ;


    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO, User user) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage ,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()) ;

            helper.setTo(notificationDTO.getRecipient());
            helper.setSubject(notificationDTO.getSubject());

            //use template if provided
            if (notificationDTO.getTemplateName() != null){
                Context  context = new Context();
                context.setVariables(notificationDTO.getTemplateVariable());
                String htmlContent = templateEngine.process(notificationDTO.getTemplateName() , context);
                helper.setText(htmlContent, true);}
            else {
                // if not template , save directly
                helper.setText(notificationDTO.getBody() , true);
            }
        mailSender.send(mimeMessage);
            log.info("Email sent Out");

        //save it to db table;
            /*Notification notificationToSave = Notification.builder().
                    recipient(notificationDTO.getRecipient())
                    .subject(notificationDTO.getSubject())
                    .body(notificationDTO.getBody())
                    .type(NotificationType.EMAIL).
                    user(user).
                    build();
            notificationRepo.save(notificationToSave);*/

        }catch (MessagingException e){
            log.error(e.getMessage());
        }
    }
}
