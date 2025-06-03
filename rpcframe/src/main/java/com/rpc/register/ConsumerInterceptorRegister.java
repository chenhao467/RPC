package com.rpc.register;

import com.rpc.interceptor.RpcInterceptor;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConsumerInterceptorRegister {

    private static final List<RpcInterceptor> interceptorList = new ArrayList<>();

    public static void register(RpcInterceptor interceptor){
        interceptorList.add(interceptor);
    }

    public static List<RpcInterceptor> getInterceptors() {
        return interceptorList;
    }
    // ✅ 自动扫描并注册所有实现类
    public static void init() {
        // 指定包名（可以传参或常量）
        Reflections reflections = new Reflections("com.rpc.interceptor");

        // 找出所有实现 RpcInterceptor 的类（非接口、非抽象类）
        Set<Class<? extends RpcInterceptor>> classes = reflections.getSubTypesOf(RpcInterceptor.class);

        for (Class<? extends RpcInterceptor> clazz : classes) {
            try {
                // 实例化并注册
                RpcInterceptor interceptor = clazz.getDeclaredConstructor().newInstance();
                register(interceptor);
                System.out.println("已注册拦截器：" + clazz.getName());
            } catch (Exception e) {
                System.err.println("注册拦截器失败：" + clazz.getName());
                e.printStackTrace();
            }
        }
    }
    public static List <RpcInterceptor> getAll(){
        List <RpcInterceptor> interceptors = new ArrayList<>();
         for (RpcInterceptor interceptor : interceptorList) {
             interceptors.add(interceptor);
        }
         return  interceptors;
    }
}
