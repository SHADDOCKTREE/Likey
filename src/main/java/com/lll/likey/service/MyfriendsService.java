package com.lll.likey.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lll.likey.bean.My_friends;
import com.lll.likey.bean.vo.MyFriendsVO;

import java.util.List;

public interface MyfriendsService extends IService<My_friends> {
    //获取所有好友
    public List<MyFriendsVO> myFriendsList(String userId);
}
