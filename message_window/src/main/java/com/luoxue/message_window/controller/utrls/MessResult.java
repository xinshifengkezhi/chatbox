package com.luoxue.message_window.controller.utrls;

import com.luoxue.message_window.domain.Messages;
import lombok.Data;

@Data
public class MessResult {
    private Boolean flag;
    private Object data;
    private String msg;

    public MessResult(Boolean flag){
        this.flag=flag;
    }

    public MessResult(Boolean flag, Object data){
        this.flag=flag;
        this.data=data;
    }
}
