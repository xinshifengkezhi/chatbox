package com.luoxue.message_window.controller.utrls;

import lombok.Data;

@Data
public class RequestResult {
    private Boolean flag;
    private Object data;
    private String msg;

    public RequestResult(Boolean flag){
        this.flag=flag;
    }

    public RequestResult(Boolean flag, Object data){
        this.flag=flag;
        this.data=data;
    }
}
