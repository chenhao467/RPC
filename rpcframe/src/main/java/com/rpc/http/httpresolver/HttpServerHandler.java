package com.rpc.http.httpresolver;

import com.rpc.common.entity.Invocation;
import com.rpc.register.LocalRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午8:55
*/
public class HttpServerHandler {
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
        String interfaceName = invocation.getInterfaceName();
        /*
        * 这里仅仅只能拿到接口名，然而我们真正想要执行的是接口对应的实现类里的方法
        * 又不想像spring一样遍历包下所有类来找出实现类，性能太低
        * 所以我们自己创建一个工具 LocalRegister 启动tomcat之前 将接口和实现类的映射关系注册进容器
        * */
        Class<?> implClass = LocalRegister.get(interfaceName,  invocation.getVersion()==null?"1.0":invocation.getVersion());
        Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
        Object result =  method.invoke(implClass.getDeclaredConstructor().newInstance(), invocation.getParameters());
        resp.setContentType("text/plain;charset=UTF-8");
        // 使用 ObjectOutputStream 写回 Object 使用 try-with-resources 语法糖 用于自动关闭资源.
        try (ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream())) {
            oos.writeObject(result);
        }


    }
}
