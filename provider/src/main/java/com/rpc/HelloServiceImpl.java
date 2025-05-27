package com.rpc;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:16
*/
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayhello(String name) {
        return "hello"+name;
    }
}
