package com.lll.likey.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //websocket基于http协议,所以需要有http编解码器
        pipeline.addLast(new HttpServerCodec());
        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合, 聚合成FullHttpRequest或FullHttpResponse
        //几乎在netty中的编程都会有此handler
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //++++++++++++++++++++以上用于支持http协议+++++++++++++++++++++++++
        /**websocket服务器处理的协议,用于指定给客户端访问的路由 ;/ws
         * 该handler处理一些复杂繁重事务
         * 处理握手动作: handshaking(close ping pong)
         * websocket都是以frames进行传输的,不同的数据类型对于frames也不同
        */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        //添加自定义助手类
        pipeline.addLast(new ChatHandler());
    }
}
