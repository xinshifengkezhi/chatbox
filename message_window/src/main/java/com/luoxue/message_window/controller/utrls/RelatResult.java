package com.luoxue.message_window.controller.utrls;

import com.luoxue.message_window.domain.Messages;
import com.luoxue.message_window.domain.UserRelations;
import lombok.Data;

@Data
public class RelatResult {
    private Boolean flag;
    private Object data;
    private String msg;

    public RelatResult(Boolean flag){
        this.flag=flag;
    }

    public RelatResult(Boolean flag, Object data){
        this.flag=flag;
        this.data=data;
    }
}
