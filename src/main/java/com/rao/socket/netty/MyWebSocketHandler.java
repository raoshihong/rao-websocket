package com.rao.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author raoshihong
 * @date 11/28/21 8:38 AM
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    // 记录客户端的channel
    private static List<Channel> channels = new ArrayList<>();

    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");

        //添加到channelGroup通道组
       channels.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");
        //添加到channelGroup 通道组
        channels.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，处理参数 by zhengkai.blog.csdn.net
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            // 判断是否是http升级为websocket
            if (!request.decoderResult().isSuccess() ||
                !"websocket".equals(request.headers().get("Upgrade"))) {
                // 不是websocket,则
                sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,HttpResponseStatus.BAD_REQUEST));
                return;
            }

            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8090/websocket",null,false);
            handshaker = wsFactory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            }else {
                // 需要建立握手协议
                handshaker.handshake(ctx.channel(),request);
            }

        }else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" +frame.text());
            sendAllMessage(frame.text());
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object textWebSocketFrame) throws Exception {

    }

    @Override public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    public static void sendAllMessage(String message){
        //收到信息后，群发给所有channel'
//        for (Channel channel : channels) {
//            channel.writeAndFlush( new TextWebSocketFrame(message));
//        }

        channels.get(0).writeAndFlush(new TextWebSocketFrame(message));
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,FullHttpRequest req, FullHttpResponse res){
        if (res.status().code()!=200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        ctx.channel().writeAndFlush(res);
    }
}
