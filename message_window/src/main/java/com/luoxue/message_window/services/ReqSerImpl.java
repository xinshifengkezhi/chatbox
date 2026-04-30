package com.luoxue.message_window.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.luoxue.message_window.domain.AddRequest;
import com.luoxue.message_window.mapper.RequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReqSerImpl implements RequestService{

    @Autowired
    private RequestMapper reqMapper;

    @Override
    public Boolean save(AddRequest request) {
        LambdaQueryWrapper<AddRequest> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AddRequest::getRequestId, request.getRequestId())
                .eq(AddRequest::getReceiverId, request.getReceiverId());
        if (reqMapper.selectCount(wrapper) > 0) {
            return false;
        }
        return reqMapper.insert(request) > 0;
    }

    @Override
    public Boolean delete(Integer requestId, Integer receiverId) {
        LambdaQueryWrapper<AddRequest> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AddRequest::getRequestId, requestId)
                .eq(AddRequest::getReceiverId, receiverId);
        return reqMapper.delete(wrapper) > 0;
    }

    @Override
    public List<AddRequest> getRequest(Integer receiverId) {
        LambdaQueryWrapper<AddRequest> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AddRequest::getReceiverId, receiverId);
        return reqMapper.selectList(wrapper);
    }
}
