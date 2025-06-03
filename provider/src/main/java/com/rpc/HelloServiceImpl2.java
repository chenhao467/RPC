package com.rpc;

import org.springframework.stereotype.Component;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午9:24
*/
@Component

public class HelloServiceImpl2 implements HelloService{
    @Override
    public String sayhello(String name) {
        return "hello2";
    }
}
