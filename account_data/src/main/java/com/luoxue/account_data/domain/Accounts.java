package com.luoxue.account_data.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("accountdatas")
public class Accounts {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String accountNum;
    private String password;
    private String username;
    private String others;

    @Override
    public String toString() {
        return "Accounts{" +
                "id=" + id +
                ", accountNum='" + accountNum + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", others='" + others + '\'' +
                "}\n";
    }
}
