package com.luoxue.message_window.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userrelations")
public class UserRelations {
    private Integer selfId;
    private Integer friendId;
    private String username;

    @Override
    public String toString() {
        return "UserRelations{" +
                "selfId=" + selfId +
                ", friendId=" + friendId +
                ", username=" + username +
                "}\n";
    }
}
