package com.lll.likey.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class Users {
    private String id;
    private String username;
    private String password;
    private String face_image;
    private String face_image_big;
    private String nickname;
    private String qrcode;
    private String cid;
}
