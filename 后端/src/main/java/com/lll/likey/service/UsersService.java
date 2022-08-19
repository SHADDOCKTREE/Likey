package com.lll.likey.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lll.likey.bean.Chat_msg;
import com.lll.likey.bean.Users;
import com.lll.likey.bean.vo.FriendRequestVO;
import com.lll.likey.bean.vo.UsersVO;

import java.util.List;

public interface UsersService extends IService<Users> {

    //保存注册用户
    public Users saveUser(Users users) throws Exception;

    //更新昵称
    public UsersVO updateNickname(Users users) throws Exception;

    // 搜索朋友的前置条件
    public Integer preSearchUser(String myUserId, String friendUsername);

    //根据用户名查询用户对象
    public Users searchUserByName(String username);

    //存储申请信息, 一个中间量, 相当于购物网站里的购物车.
    public void applyFriend(String myUserId, String friendUsername);

    //获取好友添加申请
    public List<FriendRequestVO> FriendRequestList(String MyID);

    public List<Chat_msg> getunReadList(String acceptUserId);
}
