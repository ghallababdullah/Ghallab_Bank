package com.ghallab.Ghallab_Bank;

import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.enums.NotificationType;
import com.ghallab.Ghallab_Bank.notification.dtos.NotificationDTO;
import com.ghallab.Ghallab_Bank.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class GhallabBankApplication {
	private final NotificationService notificationService ;

	public static void main(String[] args) {
		SpringApplication.run(GhallabBankApplication.class, args);
	}
	@Bean
	CommandLineRunner runner(){
		return args->{
			NotificationDTO notificationDTO = NotificationDTO.builder()
			.recipient("abdullahghallab20@gmail.com")
					.subject("Hello Testing email")
					.body("Hey this is a test email")
					.type(NotificationType.EMAIL)
					.build();
			notificationService.sendEmail(notificationDTO, new User());
		};
	}
}
