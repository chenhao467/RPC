package com.rpc;

import com.rpc.register.LocalRegister;

import java.io.IOException;
import java.net.MalformedURLException;


/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:26
*/
public class Provider {
    public static void main(String[] args) throws IOException {
        LocalRegister.register(HelloService.class.getSimpleName(), "1.0",new HelloServiceImpl());
        LocalRegister.register(HelloService.class.getSimpleName(), "2.0",new HelloServiceImpl2());
        Bootstrap.start();
    }
}
