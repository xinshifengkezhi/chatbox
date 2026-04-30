package com.luoxue.message_window.services;

import com.luoxue.message_window.domain.Messages;

import java.util.List;

public interface MessService {
    Boolean save(Messages relation);
    Boolean delete(Integer sendId, Integer receiverId, String time);
    List<Messages> getMessages(Integer senderId, Integer receiverId);
}
