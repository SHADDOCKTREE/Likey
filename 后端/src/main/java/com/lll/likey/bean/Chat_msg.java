package com.lll.likey.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("chat_msg")
public class Chat_msg {
    private String id;
    private String send_user;
    private String accept_user;
    private String msg;
    private Integer sign_flag;
    private Date create_time;
}
