package com.lll.likey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lll.likey.bean.Chat_msg;
import com.lll.likey.bean.Friends_request;
import com.lll.likey.bean.My_friends;
import com.lll.likey.bean.Users;
import com.lll.likey.bean.vo.FriendRequestVO;
import com.lll.likey.bean.vo.UsersVO;
import com.lll.likey.enums.SearchUserEnum;
import com.lll.likey.mapper.ChatmsgMapper;
import com.lll.likey.mapper.FriendsrequestMapper;
import com.lll.likey.mapper.MyfriendsMapper;
import com.lll.likey.mapper.UsersMapper;
import com.lll.likey.service.FriendsrequestService;
import com.lll.likey.service.MyfriendsService;
import com.lll.likey.service.UsersService;
import com.lll.likey.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private Sid sid;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private FriendsrequestMapper friendsrequestMapper;
    @Autowired
    private MyfriendsMapper MyfriendsMapper;
    @Autowired
    private ChatmsgMapper chatmsgMapper;

    @Override
    public Users saveUser(Users users) throws Exception{
        String usrID = sid.nextShort();
        //为每个用户生成唯一身份二维码
        users.setQrcode("");
        users.setId(usrID);
        users.setUsername(users.getUsername());
        users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
        users.setNickname("无");
        users.setFace_image("");
        users.setFace_image_big("");
        usersMapper.insert(users);
        return users;
    }

    @Override
    public UsersVO updateNickname(Users user) throws Exception {
        usersMapper.updateById(user);
        Users users = new Users();
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("id",user.getId());
        users = usersMapper.selectOne(wrapper);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        return usersVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preSearchUser(String myUserId, String friendUsername) {
        Users user = searchUserByName(friendUsername);
        if (user == null) {
            return SearchUserEnum.USER_NOT_EXIST.status;
        }
        if (user.getId().equals(myUserId)) {
            return SearchUserEnum.NOT_YOURSELF.status;
        }
        QueryWrapper<My_friends> wrapper = new QueryWrapper<>();
        wrapper.eq("myuserdi",myUserId).eq("myfrienduserdi",user.getId());
        My_friends myFriends =  MyfriendsMapper.selectOne(wrapper);
        if (myFriends != null) {
            return SearchUserEnum.ALREADY_FRIENDS.status;
        }
        return SearchUserEnum.SUCCESS.status;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users searchUserByName(String username) {
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);

        return usersMapper.selectOne(wrapper);
    }

    @Override
    public void applyFriend(String myUserId, String friendUsername) {
        Users user = searchUserByName(friendUsername);
        QueryWrapper<Friends_request> wrapper = new QueryWrapper<>();
        wrapper.eq("accept_user_id",user.getId()).eq("send_user_id",myUserId);
        if (friendsrequestMapper.selectOne(wrapper) != null){
            System.out.println("已发送过申请,不做处理");
        }else {
            Friends_request friendsRequest = new Friends_request();
            String requestID = sid.nextShort();
            friendsRequest.setId(requestID);
            friendsRequest.setSend_user_id(myUserId);
            friendsRequest.setAccept_user_id(user.getId());
            friendsRequest.setRequest_date_time(new Date());
            friendsrequestMapper.insert(friendsRequest);
        }
    }

    @Override
    public List<FriendRequestVO> FriendRequestList(String MyID) {
        return friendsrequestMapper.getApplyList(MyID);
    }

    @Override
    public List<Chat_msg> getunReadList(String acceptUserId) {
        QueryWrapper<Chat_msg> wrapper = new QueryWrapper<>();
        wrapper.eq("accept_user",acceptUserId).eq("sign_flag",0);
        List<Chat_msg> chat_msgs =  chatmsgMapper.selectList(wrapper);
        return chat_msgs;
    }
}
