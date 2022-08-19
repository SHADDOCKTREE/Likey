package com.lll.likey.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lll.likey.bean.Chat_msg;
import com.lll.likey.bean.Users;
import com.lll.likey.bean.vo.FriendRequestVO;
import com.lll.likey.bean.vo.MyFriendsVO;
import com.lll.likey.bean.vo.UsersVO;
import com.lll.likey.enums.SearchUserEnum;
import com.lll.likey.mapper.UsersMapper;
import com.lll.likey.service.FriendsrequestService;
import com.lll.likey.service.MyfriendsService;
import com.lll.likey.service.UsersService;
import com.lll.likey.utils.JSONResult;
import com.lll.likey.utils.MD5Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("u")
public class UserController {
    @Autowired
    MyfriendsService myfriendsService;
    @Autowired
    UsersService usersService;
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    private FriendsrequestService friendsrequestService;

    @PostMapping("/login")
    public JSONResult Login(@RequestBody Users users) throws Exception{
        //判断不为空
        if (StringUtils.isEmpty(users.getUsername())||StringUtils.isEmpty(users.getPassword())){
            return JSONResult.errorMsg("用户名或密码不能为空...");
        }
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",users.getUsername()).eq("password",MD5Utils.getMD5Str(users.getPassword()));
        if (usersMapper.selectList(queryWrapper).size()==0){
            return JSONResult.errorMsg("用户名或密码不正确");
        }
        Users result = usersMapper.selectOne(queryWrapper);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(result,usersVO);
        return JSONResult.ok(usersVO);
    }
    @PostMapping("/regist")
    public JSONResult Regist(@RequestBody Users users) throws Exception{
        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username",users.getUsername());
        Users result = usersMapper.selectOne(wrapper);
        if (result != null){
            return JSONResult.errorMsg("该用户名已经被注册");
        }else {
            result = usersService.saveUser(users);
        }
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(result,usersVO);
        return JSONResult.ok(usersVO);
    }
    @PostMapping("/setNickname")
    public JSONResult SetNickname(@RequestBody Users user) throws Exception {

        UsersVO usersVO = usersService.updateNickname(user);

        return JSONResult.ok(usersVO);
    }

    //搜索朋友
    @PostMapping("/search")
    public JSONResult searchUser(String myUserId, String friendUsername) throws Exception{
        if (StringUtils.isEmpty(myUserId)||StringUtils.isEmpty(friendUsername)){
            return JSONResult.errorMsg("账 号 不 能 为 空");
        }
//        return JSONResult.ok(result);
        Integer status = usersService.preSearchUser(myUserId, friendUsername);
        if (status.equals(SearchUserEnum.SUCCESS.status)){
            Users users = usersService.searchUserByName(friendUsername);
            UsersVO usersVO = new UsersVO();
            BeanUtils.copyProperties(users,usersVO);
            return JSONResult.ok(usersVO);
        }else if (status.equals(SearchUserEnum.ALREADY_FRIENDS.status)){
            return JSONResult.errorMsg(SearchUserEnum.ALREADY_FRIENDS.msg);
        }else if (status.equals(SearchUserEnum.USER_NOT_EXIST.status)){
            return JSONResult.errorMsg(SearchUserEnum.USER_NOT_EXIST.msg);
        }else {
            return JSONResult.errorMsg(SearchUserEnum.NOT_YOURSELF.msg);
        }
    }

    @PostMapping("/applyFriendRequest")
    public JSONResult ApplyFriendRequest(String myUserId, String friendUsername){
        usersService.applyFriend(myUserId,friendUsername);
        return JSONResult.ok();
    }
    @PostMapping("/queryFriendRequests")
    public JSONResult getApplyList(String MyID){
        if (StringUtils.isEmpty(MyID)){
            return JSONResult.errorMsg("");
        }
        List<FriendRequestVO> friendRequestList = usersService.FriendRequestList(MyID);
        return JSONResult.ok(friendRequestList);
    }
    @PostMapping("/operFriendRequest")
    public JSONResult operFriendRequest(String acceptUserId, String sendUserId, Integer operType){
        if (operType == 1){
            friendsrequestService.passRequest(acceptUserId,sendUserId);
        }
        friendsrequestService.removeRequest(acceptUserId,sendUserId);
        List<MyFriendsVO> myFriendsVOList = myfriendsService.myFriendsList(acceptUserId);
        return JSONResult.ok(myFriendsVOList);
    }
    @PostMapping("myFriends")
    public JSONResult myFriends(String userId){
        List<MyFriendsVO> myFriendsVOList = myfriendsService.myFriendsList(userId);
        return JSONResult.ok(myFriendsVOList);
    }
    @PostMapping("getUnReadMsgList")
    public JSONResult getUnread(String acceptUserId){
        if (StringUtils.isEmpty(acceptUserId)){
            return JSONResult.errorMsg("");
        }
        List<Chat_msg> myFriendsVOList = usersService.getunReadList(acceptUserId);
        return JSONResult.ok(myFriendsVOList);
    }

}
