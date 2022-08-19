package com.lll.likey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lll.likey.bean.Friends_request;
import com.lll.likey.bean.My_friends;
import com.lll.likey.bean.Users;
import com.lll.likey.enums.MsgType;
import com.lll.likey.mapper.FriendsrequestMapper;
import com.lll.likey.mapper.MyfriendsMapper;
import com.lll.likey.mapper.UsersMapper;
import com.lll.likey.netty.DataContent;
import com.lll.likey.netty.UCR;
import com.lll.likey.service.FriendsrequestService;
import com.lll.likey.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendsrequestServiceImpl extends ServiceImpl<FriendsrequestMapper, Friends_request> implements FriendsrequestService {

    @Autowired
    private Sid sid;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private FriendsrequestMapper friendsrequestMapper;
    @Autowired
    private MyfriendsMapper myfriendsMapper;


    @Override
    public void removeRequest(String acceptUserId, String sendUserId) {
        QueryWrapper<Friends_request> wrapper = new QueryWrapper<>();
        wrapper.eq("accept_user_id",acceptUserId).eq("send_user_id",sendUserId);
        friendsrequestMapper.delete(wrapper);
        QueryWrapper<Friends_request> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("accept_user_id", sendUserId).eq("send_user_id",acceptUserId);
        friendsrequestMapper.delete(wrapper2);
    }

    @Override
    public void passRequest(String acceptUserId, String sendUserId) {
        saveFriend(acceptUserId,sendUserId);
        saveFriend(sendUserId,acceptUserId);
        Channel schannel = UCR.get(sendUserId);
        if (UCR.get(sendUserId)!=null){
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgType.PULL_FRIEND.type);
            schannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
        }
    }

    @Override
    public void saveFriend(String acceptUserId, String sendUserId) {
        My_friends myFriends = new My_friends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyuserdi(acceptUserId);
        myFriends.setMyfrienduserdi(sendUserId);
        myfriendsMapper.insert(myFriends);
    }
}
