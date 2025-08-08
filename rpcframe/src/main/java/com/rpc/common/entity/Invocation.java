package com.rpc.common.entity;

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
    private String version;

    public Invocation(String name, String sayhello, Class[] classes, Object[] objects, String version) {
        this.interfaceName = name;
        this.methodName = sayhello;
        this.parameterTypes = classes;
        this.parameters = objects;
        this.version = version;
    }
}
