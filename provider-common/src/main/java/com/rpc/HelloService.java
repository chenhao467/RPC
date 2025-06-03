package com.rpc;

import org.springframework.stereotype.Component;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:16
*/
@Component
public interface HelloService {
    String sayhello(String name);
}
