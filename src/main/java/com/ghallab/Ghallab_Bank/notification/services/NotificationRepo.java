package com.ghallab.Ghallab_Bank.notification.services;

import com.ghallab.Ghallab_Bank.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo  extends JpaRepository<Notification , Long> {
}
