package com.rpc.proxy;

import com.rpc.Tcp.Tcpsend.TcpClient;
import com.rpc.common.entity.Invocation;
import com.rpc.common.entity.URL;
import com.rpc.common.exception.BusinessException;
import com.rpc.common.exception.ResultCodeEnum;
import com.rpc.common.interceptor.RpcInterceptor;
import com.rpc.common.loadbalance.Loadbalance;
import com.rpc.register.ConsumerInterceptorRegister;
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
        TcpClient tcpClient = new TcpClient("localhost", 8080);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Invocation invocation = new Invocation(interfaceClass.getSimpleName(), method.getName(), method.getParameterTypes(), args,"1.0");
                //获取拦截器
                List<RpcInterceptor> interceptors = ConsumerInterceptorRegister.getAll();
                // before
                for (RpcInterceptor interceptor : interceptors) {
                    interceptor.beforeInvoke(invocation);
                }


                //服务发现
                List<URL> originalList = MapRemoteRegister.get(interfaceClass.getName());
                List<URL> list = new ArrayList<>(originalList); // 拷贝


                //负载均衡
                int max = 3;
                Object result = null;
                List<URL> invokedList = new ArrayList<>();
                //服务调用

                while (max > 0) {
                    list.removeAll(invokedList);
                    //已无可用节点时，抛出异常
                    if (list.isEmpty()) {
                        throw new BusinessException(ResultCodeEnum.SERVICE_NOT_FOUND);
                    }
                    URL url = Loadbalance.random(list);
                    invokedList.add(url);
                    try {
                       // int i = 1/0;
                    result = tcpClient.send(invocation);

                    // after
                    for (RpcInterceptor interceptor : interceptors) {
                        interceptor.afterInvoke(invocation, result);
                    }
                    break;
                    }catch(Exception e){
                    for (RpcInterceptor interceptor : interceptors) {
                        interceptor.onException(invocation, e);
                    }
                        System.err.println("调用失败节点：" + url + "，剩余重试次数：" + (max - 1));
                        max--;
                }
              }
                return result;

            }
        });
        return (T) proxyInstance;
    }
}
