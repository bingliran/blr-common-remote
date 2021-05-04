package com.blr19c.common.remote.rpc.api;

import com.blr19c.common.remote.common.RpcInvocation;
import com.blr19c.common.remote.rpc.api.exception.RpcException;
import reactor.core.publisher.Mono;

/**
 * 调用空间
 *
 * @author blr
 */
public interface Invoker extends ConnectionNode {

    /**
     * 原接口
     */
    Class<?> getInterface();

    /**
     * 任务
     */
    <T> Mono<T> invoke(RpcInvocation invocation) throws RpcException;

}
