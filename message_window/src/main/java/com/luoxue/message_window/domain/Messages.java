package com.luoxue.message_window.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Timer;

@Data
@TableName("messages")
public class Messages {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String time;

    @Override
    public String toString() {
        return "Messages{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                "}\n";
    }
}
