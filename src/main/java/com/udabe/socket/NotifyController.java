package com.udabe.socket;

import com.udabe.dto.notifi.NotifyVIDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
@RequestMapping("/api/notify")
public class NotifyController {

    private final NotificationService notificationService;

    private final NotifyService notifyService;

    @Autowired
    public NotifyController(NotificationService notificationService, NotifyService notifyService) {
        this.notificationService = notificationService;
        this.notifyService = notifyService;
    }


    @MessageMapping("/messages")
    @SendTo("/topic/notify")
    public ResponseNotify getMessage(final Notify notify) throws InterruptedException {
        notificationService.sendGlobalNotification();
        return new ResponseNotify(HtmlUtils.htmlEscape(notify.getNotifyContentVi()));
    }


    @MessageMapping("/private-messages")
    @SendToUser("/topic/private-notify")
    public ResponseNotify getPrivateMessage(final Notify notify,
                                            final Principal principal){
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseNotify(HtmlUtils.htmlEscape(
                "Sending private message to user " + principal.getName() + ": "
                        + notify.getNotifyContentVi())
        );
    }


    @GetMapping
    public ResponseEntity<?> findAllNotify(@RequestParam(value = "userId") Long userId, @RequestParam(value = "language", required = false) String language) {
        return new ResponseEntity<>(notifyService.findAll(userId, language), HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<NotifyVIDTO> findNotifyById(@PathVariable("id") Long notifyId) {
        return new ResponseEntity<>(notifyService.findById(notifyId), HttpStatus.OK);
    }


    @PostMapping("/disable")
    public ResponseEntity<String> disable(@RequestParam Long notifyId, @RequestParam String statusDelete) {
        notifyService.disable(notifyId, statusDelete);
        return ResponseEntity.ok("Successfully.");
    }


    @PostMapping("/disableAll")
    public ResponseEntity<String> disableAll(@RequestParam Long userId, @RequestParam String statusDelete) {
        notifyService.disableAll(userId, statusDelete);
        return ResponseEntity.ok("Successfully.");
    }


    @PostMapping("/markAllRead")
    public ResponseEntity<String> markAllRead(@RequestParam Long userId) {
        notifyService.markAllRead(userId);
        return ResponseEntity.ok("Successfully.");
    }


    @PostMapping("/markAsRead")
    public ResponseEntity<String> markAsRead(@RequestParam Long notifyId, @RequestParam boolean status){
        notifyService.markAsRead(notifyId, status);
        return ResponseEntity.ok("Successfully");
    }


    @PostMapping("/checkNotify")
    public ResponseEntity<String> checkNotify(@RequestParam Long userId) {
        notifyService.checkNotify(userId);
        return ResponseEntity.ok("Successfully.");
    }

}


/*@MessageMapping là một Annotation của Spring để đánh dấu phương thức xử lý tin nhắn được gửi đến từ một WebSocket client. @MessageMapping("/message")
đánh dấu phương thức getMessage() để xử lý tin nhắn được gửi đến địa chỉ "/message".

@SendTo là một Annotation của Spring được sử dụng để gửi lại kết quả từ phương thức xử lý tin nhắn đến tất cả các client đã đăng ký theo địa chỉ được
chỉ định. @SendTo("/topic/messages") sẽ gửi kết quả từ phương thức getMessage() đến tất cả các client đã đăng ký theo địa chỉ "/topic/messages".

Phương thức getMessage() nhận một đối tượng Message từ client và trả về một ResponseMessage được truyền vào constructor của đối tượng
ResponseMessage. Phương thức này sử dụng HtmlUtils.htmlEscape() để tránh việc mã độc được chèn vào tin nhắn.

@MessageMapping("/private-message") đánh dấu phương thức getPrivateMessage() để xử lý tin nhắn được gửi đến địa chỉ "/private-message". @SendToUser
là một Annotation của Spring được sử dụng để gửi lại kết quả từ phương thức xử lý tin nhắn đến client đã gửi tin nhắn đó. @SendToUser("/topic/private-messages") sẽ gửi kết quả từ phương thức
getPrivateMessage() đến client đã gửi tin nhắn theo địa chỉ "/topic/private-messages".

Phương thức getPrivateMessage() cũng nhận một đối tượng Message từ client và một Principal, đại diện cho người dùng đã gửi tin nhắn. N
ó sử dụng HtmlUtils.htmlEscape() để tránh việc mã độc được chèn vào tin nhắn và trả về một ResponseMessage. Sau đó, phương thức này gọi
notificationService.sendPrivateNotification() để thông báo cho NotificationService gửi một thông báo riêng cho người dùng đã gửi tin nhắn.*/
