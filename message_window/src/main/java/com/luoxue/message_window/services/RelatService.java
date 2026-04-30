package com.luoxue.message_window.services;

import com.luoxue.message_window.domain.UserRelations;

import java.util.List;


public interface RelatService {
    Boolean save(UserRelations relation);
    Boolean update(Integer friendId, String name);
    Boolean delete(Integer selfId, Integer friendId);
    Boolean deleteAll(Integer selfId);
    Boolean getFriend(Integer selfId, Integer friendId);
    List<UserRelations> getFriends(Integer selfId);
    List<UserRelations> chatFriends(String username);

}
