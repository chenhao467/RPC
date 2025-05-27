package com.rpc.common;

import lombok.Data;

import java.io.Serializable;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午9:02
*/
@Data
public class Invocation implements Serializable {
    //表示当前调用的是哪个接口的哪个方法
    private String interfaceName;
    private String methodName;
    private Class[] parameterTypes; //参数类型
    private Object[] parameters;  //具体参数
    private String version = "1.0";

    public Invocation(String name, String sayhello, String s, Class[] classes, Object[] objects) {
        this.interfaceName = name;
        this.methodName = sayhello;
        this.parameterTypes = classes;
        this.parameters = objects;
        if(s != null)
            this.version = s;
    }
}
