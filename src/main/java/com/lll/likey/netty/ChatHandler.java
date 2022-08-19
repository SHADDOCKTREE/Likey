package com.lll.likey.netty;

import com.alibaba.druid.util.StringUtils;
import com.lll.likey.SpringUtil;
import com.lll.likey.enums.MsgType;
import com.lll.likey.service.ChatmsgService;
import com.lll.likey.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.catalina.LifecycleState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TextWebsocketFrame: 在netty中, 用于为websoccket专门处理文本的对象,frame是消息的载体
 */

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    //用于记录和管理,记录所有客户端的channel
    private static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当客户端链接服务端后,获取客户端channel,放到channelgroup中管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //移除是自动执行的,不用写没问题
        //clients.remove(ctx.channel());
        System.out.println("客户端断开, channel对应的id: "
                +ctx.channel().id().asLongText());
        clients.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //消息发送失败后, 关闭channel并移除
        clients.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端传来的消息
        String content = msg.text();
        Channel currentChannel = ctx.channel();
        System.out.println("接收到的数据: " + content);
        for (Channel channel:clients){
            channel.writeAndFlush(
                    new TextWebSocketFrame("[服务器接受到消息: ]" + LocalDateTime.now()
                    + "接收到消息为: " + content));
        }
        DataContent dataContent = JsonUtils.jsonToPojo(content,DataContent.class);
        Integer action = dataContent.getAction();

        if (action.equals(MsgType.CONNECT.getType())){
            //初始化channel, 关联其和userid
            String senderID =  dataContent.getChatMsg().getSenderId();
            UCR.put(senderID, currentChannel);

            //test
            for (Channel c:clients){
                System.out.println(c.id().asLongText());
            }
            UCR.output();

        }else if (action.equals(MsgType.CHAT.getType())){
            ChatMsg chatMsg =  dataContent.getChatMsg();
            String mag2 = chatMsg.getMsg();
            String receiver = chatMsg.getReceiverId();
            String senderId = chatMsg.getSenderId();

            ChatmsgService chatmsgService = (ChatmsgService) SpringUtil.getBean("chatmsgServiceImpl");
            String msgID =  chatmsgService.saveMsg(chatMsg);

            chatMsg.setMsgId(msgID);
            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setChatMsg(chatMsg);

            Channel receiverChannel =  UCR.get(receiver);
            if (receiverChannel != null) {
                if(clients.find(receiverChannel.id()) != null){
                    //表示用户在线
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContentMsg)));
                }
            }
        }else if(action.equals(MsgType.SIGNED.getType())){
            //将未读信息转为已读
            ChatmsgService chatmsgService = (ChatmsgService) SpringUtil.getBean("chatmsgServiceImpl");
            String msgID2 =  dataContent.getExtand();
            String msgID2_list[] = msgID2.split(",");
            List<String> msgIdList = new ArrayList<>();
            for (String mid : msgID2_list){
                if (!(StringUtils.isEmpty(mid))){
                    msgIdList.add(mid);
                }
            }
            System.out.println(msgIdList.toString());
            if (msgIdList != null && !msgIdList.isEmpty() &&msgIdList.size()>0){
                chatmsgService.readMsg(msgIdList);
            }
        }
    }
}
