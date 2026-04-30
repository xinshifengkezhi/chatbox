package com.luoxue.message_window.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.luoxue.message_window.domain.Messages;
import com.luoxue.message_window.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessSerImpl implements MessService{

    @Autowired
    private MessageMapper message;

    @Override
    public Boolean save(Messages relation) {
        return message.insert(relation) > 0;
    }

    @Override
    public Boolean delete(Integer sendId, Integer receiverId, String time) {
        LambdaQueryWrapper<Messages> wrapper = Wrappers.<Messages>lambdaQuery();
        wrapper.eq(Messages::getSenderId, sendId)
                .eq(Messages::getReceiverId, receiverId)
                .eq(Messages::getContent, time);

        return message.delete(wrapper) > 0;
    }

    @Override
    public List<Messages> getMessages(Integer senderId, Integer receiverId) {
        LambdaQueryWrapper<Messages> wrapper = Wrappers.lambdaQuery();
        wrapper.and(w -> w.eq(Messages::getSenderId, senderId).eq(Messages::getReceiverId, receiverId))
                .or(w -> w.eq(Messages::getSenderId, receiverId).eq(Messages::getReceiverId, senderId))
                .orderByAsc(Messages::getTime);
        return message.selectList(wrapper);
    }
}
