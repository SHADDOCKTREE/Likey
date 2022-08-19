package com.lll.likey.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lll.likey.bean.Friends_request;

public interface FriendsrequestService extends IService<Friends_request> {

    //删除好友添加请求记录
    public void removeRequest(String acceptUserId, String sendUserId);
    //保存新好友
    public void passRequest(String acceptUserId, String sendUserId);
    public void saveFriend(String acceptUserId, String sendUserId);

}
