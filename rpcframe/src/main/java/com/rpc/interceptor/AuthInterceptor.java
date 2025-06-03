package com.rpc.interceptor;

import com.rpc.common.Invocation;

public class AuthInterceptor implements RpcInterceptor {
    @Override
    public void beforeInvoke(Invocation invocation) {
        System.out.println("Auth 拦截器：准备调用 " + invocation.getMethodName());
        // 可添加 token 校验逻辑等
    }

    @Override
    public void afterInvoke(Invocation invocation, Object result) {
        System.out.println("Auth 拦截器：调用完成 " + invocation.getMethodName());
    }

    @Override
    public void onException(Invocation invocation, Throwable throwable) {
        System.out.println("Auth 拦截器：调用 " + invocation.getMethodName() + " 出现异常");
        System.out.println("异常信息: " + throwable.getClass().getSimpleName() + " - " + throwable.getMessage());

        // 如果是权限相关异常（举例）
        if (throwable instanceof SecurityException) {
            throw new RuntimeException("权限校验失败，请检查认证信息");
        }

        // 其他异常可忽略或记录
    }



}
