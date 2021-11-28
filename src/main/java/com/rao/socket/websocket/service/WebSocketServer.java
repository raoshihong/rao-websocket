package com.rao.socket.websocket.service;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raoshihong
 * @date 11/27/21 10:34 PM
 */
@ServerEndpoint("/imserver/{userId}")
@Component
public class WebSocketServer {

    private static List<Session> sessions = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session){
        // 保存客户端的session会话
        System.out.println(this);
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session){
        //  接收到客户端发来的消息
        System.out.println(message);
    }

    @OnError
    public void onError(Session session, Throwable error){

    }

    /**
     * 向指定的客户端发送消息
     */
    public static void sendMessage() throws Exception{

//        sessions.forEach(session -> {
//            try {
//                session.getBasicRemote().sendText("aaaa");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        });

        sessions.get(0).getBasicRemote().sendText("test");
    }
}
