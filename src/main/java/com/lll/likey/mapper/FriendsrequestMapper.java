package com.lll.likey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lll.likey.bean.Friends_request;
import com.lll.likey.bean.vo.FriendRequestVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendsrequestMapper extends BaseMapper<Friends_request> {

    @Select("SELECT users.id, username, face_image, nickname FROM friends_request, users where friends_request.send_user_id=users.id AND friends_request.accept_user_id = #{myID}")
    List<FriendRequestVO> getApplyList(@Param("myID") String myID);
}
