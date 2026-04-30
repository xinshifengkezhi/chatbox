package com.luoxue.message_window.services;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.luoxue.message_window.domain.Messages;
import com.luoxue.message_window.domain.UserRelations;
import com.luoxue.message_window.mapper.UserRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RelatSerImpl implements RelatService{

    @Autowired
    private UserRelationMapper userRelate;

    @Override
    public Boolean save(UserRelations relation) {
        LambdaQueryWrapper<UserRelations> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRelations::getSelfId, relation.getSelfId())
                .eq(UserRelations::getFriendId, relation.getFriendId());
        if (userRelate.selectCount(wrapper) > 0) {
            return false;
        }
        return userRelate.insert(relation) > 0;
    }

    @Override
    public Boolean update(Integer friendId, String name){

        UserRelations updateEntity = new UserRelations();
        updateEntity.setUsername(name);

        LambdaUpdateWrapper<UserRelations> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserRelations::getFriendId, friendId);

        return userRelate.update(updateEntity, updateWrapper) > 0;
    }

    @Override
    public Boolean delete(Integer selfId, Integer friendId) {
        LambdaQueryWrapper<UserRelations> wrapper = Wrappers.<UserRelations>lambdaQuery();
        wrapper.eq(UserRelations::getSelfId, selfId)
                .eq(UserRelations::getFriendId, friendId);

        return userRelate.delete(wrapper) > 0;
    }

    @Override
    public Boolean deleteAll(Integer selfId){
        LambdaQueryWrapper<UserRelations> wrapper = Wrappers.<UserRelations>lambdaQuery();
        wrapper.eq(UserRelations::getSelfId, selfId);
        return userRelate.delete(wrapper) > 0;
    }

    @Override
    public Boolean getFriend(Integer selfId, Integer friendId){
        LambdaQueryWrapper<UserRelations> wrapper = Wrappers.<UserRelations>lambdaQuery();
        wrapper.eq(UserRelations::getSelfId, selfId)
                .eq(UserRelations::getFriendId, friendId);
        return userRelate.selectCount(wrapper) > 0;
    }

    @Override
    public List<UserRelations> getFriends(Integer selfId) {
        LambdaQueryWrapper<UserRelations> wrapper = Wrappers.<UserRelations>lambdaQuery();
        wrapper.eq(UserRelations::getSelfId, selfId);

        return userRelate.selectList(wrapper);
    }

    @Override
    public List<UserRelations> chatFriends(String username){
        LambdaQueryWrapper<UserRelations> wrapper = Wrappers.<UserRelations>lambdaQuery();
        wrapper.like(UserRelations::getUsername, username);

        return userRelate.selectList(wrapper);
    }
}
