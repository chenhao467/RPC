package com.rpc;

import com.rpc.register.LocalRegister;

import java.net.MalformedURLException;


/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:26
*/
public class Provider {
    public static void main(String[] args) throws MalformedURLException {
        LocalRegister.register(HelloService.class.getName(), "1.0",HelloServiceImpl.class);
        LocalRegister.register(HelloService.class.getName(), "2.0",HelloServiceImpl2.class);
        Bootstrap.start();
    }
}
