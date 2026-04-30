package com.luoxue.message_window.controller;

import com.luoxue.message_window.controller.utrls.MessResult;
import com.luoxue.message_window.domain.Messages;
import com.luoxue.message_window.services.MessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/Messages")
public class MessagesController {

    @Autowired
    private MessService messSer;

    @PostMapping
    public MessResult save(@RequestBody Messages message){
        return new MessResult(messSer.save(message));
    }

    @DeleteMapping("/{senderId}/{receiverId}/{time}")
    public MessResult delMessage(@PathVariable Integer senderId,
                                 @PathVariable Integer receiverId,
                                 @PathVariable String time){
        return new MessResult(messSer.delete(senderId, receiverId, time));
    }

    @GetMapping("/{senderId}/{receiverId}")
    public MessResult getMessage(@PathVariable Integer senderId,
                                 @PathVariable Integer receiverId){
        return new MessResult(true, messSer.getMessages(senderId, receiverId));
    }

}
