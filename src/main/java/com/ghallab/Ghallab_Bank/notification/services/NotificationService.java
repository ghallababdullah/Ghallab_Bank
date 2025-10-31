package com.ghallab.Ghallab_Bank.notification.services;

import com.ghallab.Ghallab_Bank.auth_users.entity.User;
import com.ghallab.Ghallab_Bank.notification.dtos.NotificationDTO;
import com.ghallab.Ghallab_Bank.notification.entity.Notification;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO , User user) ;
}
