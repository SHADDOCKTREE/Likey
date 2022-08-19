package com.lll.likey.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lll.likey.bean.Chat_msg;
import com.lll.likey.mapper.ChatmsgMapper;
import com.lll.likey.netty.ChatMsg;
import com.lll.likey.service.ChatmsgService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ChatmsgServiceImpl extends ServiceImpl<ChatmsgMapper, Chat_msg> implements ChatmsgService {

    @Autowired
    private ChatmsgMapper chatmsgMapper;
    @Autowired
    private Sid sid;


    @Override
    public String saveMsg(ChatMsg chatMsg) {
         String msgID = sid.nextShort();
         Chat_msg chat_msg = new Chat_msg();
         chat_msg.setId(msgID);
         chat_msg.setAccept_user(chatMsg.getReceiverId());
         chat_msg.setCreate_time(new Date());
         chat_msg.setSign_flag(0);
         chat_msg.setSend_user(chatMsg.getSenderId());
         chat_msg.setMsg(chatMsg.getMsg());
         chatmsgMapper.insert(chat_msg);
        return msgID;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void readMsg(List<String> msg) {

        for (String m : msg){
            Chat_msg chat_msg = chatmsgMapper.selectById(m);
            chat_msg.setSign_flag(1);
            System.out.println(chat_msg);
            chatmsgMapper.updateById(chat_msg);
        }
    }
}
