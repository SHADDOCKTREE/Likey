package com.lll.likey.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("my_friends")
public class My_friends {
    private String id;
    private String myuserdi;
    private String myfrienduserdi;
}
