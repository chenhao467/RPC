package com.rpc;

import com.rpc.common.Invocation;
import com.rpc.interceptor.AuthInterceptor;
import com.rpc.interceptor.ExceptionLoggingInterceptor;
import com.rpc.protocol.HttpClient;
import com.rpc.protocol.HttpServer;
import com.rpc.proxy.ProxyFactory;
import com.rpc.register.ConsumerInterceptorRegister;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:21
*/
@SpringBootApplication
public class Consumer {
    public static void main(String[] args) {


        HelloService helloService =  ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayhello("chenhao");
        System.out.println(result);

    }
}
