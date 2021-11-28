package com.rao.socket.controller;

import com.rao.socket.netty.MyWebSocketHandler;
import com.rao.socket.websocket.service.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author raoshihong
 * @date 11/27/21 10:32 PM
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() throws Exception{
        WebSocketServer.sendMessage();
        return "success";
    }

    @GetMapping("/testNettySocket")
    public String testNetty(){
        MyWebSocketHandler.sendAllMessage("dsdfsdf");
        return "success";
    }
}
