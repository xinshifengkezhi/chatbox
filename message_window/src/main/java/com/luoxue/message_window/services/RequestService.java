package com.luoxue.message_window.services;

import com.luoxue.message_window.domain.AddRequest;

import java.util.List;

public interface RequestService {
    Boolean save(AddRequest request);
    Boolean delete(Integer requestId, Integer receiverId);
    List<AddRequest> getRequest(Integer receiverId);
}
