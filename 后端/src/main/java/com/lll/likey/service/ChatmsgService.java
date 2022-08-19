package com.lll.likey.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lll.likey.bean.Chat_msg;
import com.lll.likey.netty.ChatMsg;

import java.util.List;


public interface ChatmsgService extends IService<Chat_msg> {
    //村聊天记录
    public String saveMsg(ChatMsg chatMsg);
    //标记已读消息
    public void readMsg(List<String> msg);
}
