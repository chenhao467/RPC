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

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyFactory {

    // 按目标URL缓存TcpClient（一个URL对应一个连接池）
    private static final Map<URL, TcpClient> clientCache = new ConcurrentHashMap<>();

    public static <T> T getProxy(Class<T> interfaceClass) throws IOException {
        // 注册拦截器
        ConsumerInterceptorRegister.init();

        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RpcInvocationHandler(interfaceClass)
        );
    }

    private static class RpcInvocationHandler implements InvocationHandler {
        private final Class<?> interfaceClass;
        private final List<RpcInterceptor> interceptors;

        public RpcInvocationHandler(Class<?> interfaceClass) {
            this.interfaceClass = interfaceClass;
            this.interceptors = ConsumerInterceptorRegister.getAll();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Invocation invocation = new Invocation(
                    interfaceClass.getSimpleName(),
                    method.getName(),
                    method.getParameterTypes(),
                    args,
                    "1.0"
            );

            callBefore(invocation);

            List<URL> serviceList = MapRemoteRegister.get(interfaceClass.getName());
            if (serviceList == null || serviceList.isEmpty()) {
                throw new BusinessException(ResultCodeEnum.SERVICE_NOT_FOUND);
            }

            List<URL> tried = new ArrayList<>();
            int retries = 3;
            Exception lastEx = null;

            for (int i = 0; i < retries; i++) {
                // 挑一个节点
                List<URL> candidates = new ArrayList<>(serviceList);
                candidates.removeAll(tried);
                if (candidates.isEmpty()) break;

                URL target = Loadbalance.random(candidates);
                tried.add(target);

                try {
                    TcpClient client = getOrCreateClient(target);
                    Object result = client.send(invocation);
                    callAfter(invocation, result);
                    return result;
                } catch (Exception e) {
                    lastEx = e;
                    callException(invocation, e);
                    System.err.println("调用失败节点：" + target + "，剩余重试次数：" + (retries - i - 1));
                }
            }

            throw new BusinessException(ResultCodeEnum.SERVICE_NOT_FOUND, lastEx);
        }

        private TcpClient getOrCreateClient(URL url) {
            return clientCache.computeIfAbsent(url, u ->
                    {
                        try {
                            return new TcpClient(u.getHostname(), u.getPort(), 10);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }

        private void callBefore(Invocation invocation) {
            for (RpcInterceptor i : interceptors) i.beforeInvoke(invocation);
        }

        private void callAfter(Invocation invocation, Object result) {
            for (RpcInterceptor i : interceptors) i.afterInvoke(invocation, result);
        }

        private void callException(Invocation invocation, Exception e) {
            for (RpcInterceptor i : interceptors) i.onException(invocation, e);
        }
    }
}
