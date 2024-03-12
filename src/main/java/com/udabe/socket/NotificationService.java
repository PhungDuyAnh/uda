package com.udabe.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification() {
        ResponseNotify message = new ResponseNotify("Global Notification");

        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(final String userId) {
        ResponseNotify message = new ResponseNotify("Private Notification");

        messagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications", message);
    }
}
