package com.udabe.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class WSController {

    private final WSService service;

    @Autowired
    public WSController(WSService service) {
        this.service = service;
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final Notify notify) {
        service.notifyFrontend(notify);
    }

    @PostMapping("/send-private-message")
    public void sendPrivateMessage(@RequestBody final Notify notify) {
        service.notifyUser(notify);
    }

}
