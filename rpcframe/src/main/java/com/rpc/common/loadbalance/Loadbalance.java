package com.rpc.common.loadbalance;

import com.rpc.common.entity.URL;

import java.util.List;
import java.util.Random;

/*
*功能：
 作者：chenhao
*日期： 2025/6/2 下午8:04
*/
public class Loadbalance {
    public static URL random(List<URL> urls){
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }
}
