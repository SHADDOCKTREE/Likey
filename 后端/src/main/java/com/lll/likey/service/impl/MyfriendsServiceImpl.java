package com.lll.likey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lll.likey.bean.Friends_request;
import com.lll.likey.bean.My_friends;
import com.lll.likey.bean.Users;
import com.lll.likey.bean.vo.MyFriendsVO;
import com.lll.likey.mapper.MyfriendsMapper;
import com.lll.likey.mapper.UsersMapper;
import com.lll.likey.service.MyfriendsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyfriendsServiceImpl extends ServiceImpl<MyfriendsMapper, My_friends> implements MyfriendsService {

    @Autowired
    private MyfriendsMapper myfriendsMapper;
    @Autowired
    private UsersMapper usersMapper;

    @Transactional (propagation =Propagation.SUPPORTS)
    @Override
    public List<MyFriendsVO> myFriendsList(String userId) {

        System.out.println("传过来的userId: "+userId);

        QueryWrapper<My_friends> wrapper = new QueryWrapper<>();
        wrapper.eq("myuserdi",userId);
        List<My_friends> friendsList =  myfriendsMapper.selectList(wrapper);
        int length = friendsList.size();
//        System.out.println("我拥有朋友的数量:"+ length);
//        System.out.println("我朋友的id" + friendsList.get(0).getMyfrienduserdi());
//        System.out.println("我的id" + friendsList.get(0).getMyuserdi());

        List<MyFriendsVO> myFriendsVOList = new ArrayList<>();
        if (length>0){
            for (My_friends friends : friendsList){

                System.out.println("friends表里id " + friends.getId());

                Users users = usersMapper.selectById(friends.getMyfrienduserdi());

                System.out.println("我朋友的id 第三份 " + users.getId());

                MyFriendsVO myFriendsVO = new MyFriendsVO();

                myFriendsVO.setFriendUserId(users.getId());
                myFriendsVO.setFriendNickname(users.getNickname());
                myFriendsVO.setFriendUsername(users.getUsername());
                myFriendsVO.setFriendFaceImage(users.getFace_image());
                myFriendsVOList.add(myFriendsVO);
            }
        }
        return myFriendsVOList;
    }
}
