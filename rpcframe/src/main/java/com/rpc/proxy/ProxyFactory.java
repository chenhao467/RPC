package com.rpc.proxy;

import com.rpc.common.Invocation;
import com.rpc.common.URL;
import com.rpc.exception.BusinessException;
import com.rpc.exception.ResultCodeEnum;
import com.rpc.interceptor.AuthInterceptor;
import com.rpc.interceptor.ExceptionLoggingInterceptor;
import com.rpc.interceptor.RpcInterceptor;
import com.rpc.loadbalance.Loadbalance;
import com.rpc.protocol.HttpClient;
import com.rpc.register.ConsumerInterceptorRegister;
import com.rpc.register.LocalRegister;
import com.rpc.register.MapRemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/*
*功能：
 作者：chenhao
*日期： 2025/5/27 下午9:10
*/
public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass)
    {
        //注册拦截器
        ConsumerInterceptorRegister.init();
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
        HttpClient httpClient = new HttpClient();

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args);
                //获取拦截器
                List<RpcInterceptor> interceptors = ConsumerInterceptorRegister.getAll();
                // before
                for (RpcInterceptor interceptor : interceptors) {
                    interceptor.beforeInvoke(invocation);
                }
                int max = 3;

                //服务发现
                List<URL> list = MapRemoteRegister.get(interfaceClass.getName());

                //负载均衡
                Object result = null;
                List<URL> invokedList = new ArrayList<>();

                //服务调用

                while (max > 0) {
                    list.remove(invokedList);
                    URL url = Loadbalance.random(list);
                    invokedList.add(url);
                    try {
                    result = httpClient.send(url.getHostname(), url.getPort(), invocation);


                    // after
                    for (RpcInterceptor interceptor : interceptors) {
                        interceptor.afterInvoke(invocation, result);
                    }
                    break;
                }catch(Exception e){
                    for (RpcInterceptor interceptor : interceptors) {
                        interceptor.onException(invocation, e);
                    }
                    if (max-- != 0)
                        continue;


                }
              }
                return result;

            }
        });
        return (T) proxyInstance;
    }
}
