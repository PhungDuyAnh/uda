package com.udabe.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/*Cho phép gửi thông báo tới khách hàng thông qua WebSocket.*/
@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;


    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final Notify notify) {
        ResponseNotify response = new ResponseNotify(notify.getNotifyContentVi());
        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("/topic/notify", response);
    }


    public void notifyUser(final Notify notify) {
        ResponseNotify response = new ResponseNotify(notify.getNotifyContentVi());
        for(String x : notify.getUserNotifyLst()){
            notificationService.sendPrivateNotification(x);
            messagingTemplate.convertAndSendToUser(x, "/topic/private-notify", response);
        }
    }
}

/*
SimpMessagingTemplate là một Spring class được sử dụng để gửi thông báo đến các khách hàng thông qua WebSocket.
NotificationService là một service khác được sử dụng để gửi thông báo tới khách hàng.*/



/*
Phương thức notifyFrontend() được sử dụng để gửi thông báo tới tất cả các khách hàng đang kết nối với địa chỉ "/topic/messages".
Nó tạo ra một đối tượng ResponseMessage để đóng gói thông báo và gọi phương thức convertAndSend() của SimpMessagingTemplate để gửi thông báo.*/


/*
Phương thức notifyUser() được sử dụng để gửi thông báo riêng tới một khách hàng cụ thể, được xác định bằng id của họ. Nó cũng tạo ra một đối tượng
ResponseMessage và gọi phương thức convertAndSendToUser() của SimpMessagingTemplate để gửi thông báo đến địa chỉ "/topic/private-messages".
Khi gửi thông báo riêng tới một khách hàng, phương thức này cũng gọi phương thức sendPrivateNotification() của NotificationService để thông báo
cho khách hàng rằng họ có một tin nhắn mới.*/
