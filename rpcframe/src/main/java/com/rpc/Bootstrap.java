package com.rpc;

import com.rpc.common.URL;
import com.rpc.protocol.HttpServer;
import com.rpc.register.MapRemoteRegister;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
*功能：
 作者：chenhao
*日期： 2025/6/2 下午9:20
*/
public class Bootstrap {
    public static void start(){
        //注册中心注册
        /*
         * 注册中心三要素：
         *  1.多个服务间共享数据
         * 一般使用中间件如redis存储共享数据。本项目由于服务都在同一台机器，所以使用本地文件存储
         *  2.心跳机制， 检测服务是否存活
         *  未实现
         *  3.本地缓存，数据变更的监听机制.不至于每次调用服务都去服务中心找对应URL
         *  未实现
         * */

        URL url = new URL("localhost",8080);
        MapRemoteRegister.register(HelloService.class.getName(),url);

        HttpServer httpServer = new HttpServer();
        //启动HelloService模块对应的http服务
        httpServer.start(url.getHostname(),url.getPort());
    }
}
