package com.luoxue.message_window.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("addrequest")
public class AddRequest {
    private Integer requestId;
    private Integer receiverId;
    private String applicantName;
    private String time;
    private String message;

    @Override
    public String toString() {
        return "AddRequest{" +
                "requestId=" + requestId +
                ", receiverId=" + receiverId +
                ", applicantNum=" + applicantName +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                "}\n";
    }
}
