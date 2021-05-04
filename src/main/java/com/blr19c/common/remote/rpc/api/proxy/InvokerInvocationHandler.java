package com.blr19c.common.remote.rpc.api.proxy;

import com.blr19c.common.remote.rpc.api.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invoker 运行器
 *
 * @author blr
 */
public class InvokerInvocationHandler implements InvocationHandler {
    private final Invoker invoker;

    public InvokerInvocationHandler(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
