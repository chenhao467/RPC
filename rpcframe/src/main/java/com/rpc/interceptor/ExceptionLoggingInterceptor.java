package com.rpc.interceptor;

import com.rpc.common.Invocation;

public class ExceptionLoggingInterceptor implements RpcInterceptor {
    @Override
    public void beforeInvoke(Invocation invocation) {}

    @Override
    public void afterInvoke(Invocation invocation, Object result) {}

    @Override
    public void onException(Invocation invocation, Throwable throwable) {
        System.out.println("Exception异常拦截器:发生异常：" + throwable.getMessage());
    }
}
