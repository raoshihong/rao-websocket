package com.rao.socket;

import com.rao.socket.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author raoshihong
 * @date 11/27/21 10:30 PM
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        try {
            new NettyServer(8090).start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
