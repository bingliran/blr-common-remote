package com.blr19c.common.remote.rpc.api.proxy;

import com.blr19c.common.remote.rpc.api.Invoker;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * jdk代理
 *
 * @author blr
 */
public class JdkProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Invoker invoker, Class<?>[] interfaces) {
        interfaces = Arrays.copyOf(interfaces, interfaces.length + 1);
        interfaces[interfaces.length - 1] = RemoteJdkProxy.class;
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                interfaces,
                new InvokerInvocationHandler(invoker)
        );
    }

    public static boolean isProxy(Object obj) {
        return obj instanceof RemoteJdkProxy && Proxy.isProxyClass(obj.getClass());
    }


}
