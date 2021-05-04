package com.blr19c.common.remote.rpc.api.proxy;

import com.blr19c.common.remote.rpc.api.Invoker;

/**
 * 标记代表是由{@link JdkProxyFactory#getProxy(Invoker, Class[])}创建的
 *
 * @see org.springframework.aop.SpringProxy
 */
public interface RemoteJdkProxy {

}