package com.rpc;

import com.rpc.common.Invocation;
import com.rpc.protocol.HttpClient;
import com.rpc.protocol.HttpServer;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:21
*/
public class Consumer {
    public static void main(String[] args) {
        Invocation invocation = new Invocation(HelloService.class.getName(),"sayhello","1.0",new Class[]{String.class},new Object[]{"chenhao"});
        HttpClient httpClient = new HttpClient();
        String result = httpClient.send("localhost",8080,invocation);
        System.out.println(result);
    }
}
