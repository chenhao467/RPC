package com.rpc.common.interceptor;

import com.rpc.common.entity.Invocation;

public interface RpcInterceptor {
    void beforeInvoke(Invocation invocation);
    void afterInvoke(Invocation invocation, Object result);
    void onException(Invocation invocation, Throwable throwable); // 新增方法
}

