package com.rpc;

import com.rpc.protocol.HttpServer;
import com.rpc.register.LocalRegister;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:26
*/
public class Provider {
    public static void main(String[] args) {
        LocalRegister.register(HelloService.class.getName(), "1.0",HelloServiceImpl.class);
        LocalRegister.register(HelloService.class.getName(), "2.0",HelloServiceImpl2.class);
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",8080);
    }
}
