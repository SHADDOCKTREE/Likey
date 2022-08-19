package com.lll.likey.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("friends_request")
public class Friends_request {
    private String id;
    private String send_user_id;
    private String accept_user_id;
    private Date request_date_time;
}
