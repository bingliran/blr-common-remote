package com.blr19c.common.remote.rpc.api;

import com.blr19c.common.remote.common.RpcInvocation;
import com.blr19c.common.remote.rpc.api.exception.RpcException;
import reactor.core.publisher.Mono;

public class RpcInvoke implements Invoker {
    @Override
    public Class<?> getInterface() {
        return null;
    }

    @Override
    public <T> Mono<T> invoke(RpcInvocation invocation) throws RpcException {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void destroy() throws Exception {

    }
}
