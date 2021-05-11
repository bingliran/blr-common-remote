package com.blr19c.common.remote.remoting;

import com.blr19c.common.remote.common.Result;
import com.blr19c.common.remote.common.RpcInvocation;
import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.load.UniversalLoader;
import com.blr19c.common.remote.registry.RemoteRegistryHolder;
import com.blr19c.common.remote.serialization.Serialization;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 提供本地调用 抽象远程处理
 *
 * @author blr
 * @since 2021.5.10
 */
public abstract class AbstractRemotingHandler {
    protected final Serialization serialization;

    protected AbstractRemotingHandler(Serialization serialization) {
        this.serialization = serialization;
    }

    protected Result invocation(InputStream source) {
        return invocation(source, defaultOnError());
    }

    protected Result invocation(InputStream source, Function<Throwable, Result> onError) {
        try {
            RpcInvocation rpcInvocation = serialization.deserialize(source, RpcInvocation.class);
            //本地调用
            RpcURI rpcURI = RemoteRegistryHolder.getRpcURI(rpcInvocation.getServiceName(), rpcInvocation.getType());
            Object target = UniversalLoader.getLoadedLocal(rpcURI.getType());
            Method invokerMethod = rpcURI.getType().getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParameterTypes());
            Object data = invokerMethod.invoke(target, rpcInvocation.getArgs());
            return Result.success(data);
        } catch (Throwable t) {
            return onError.apply(t);
        }
    }

    protected Function<Throwable, Result> defaultOnError() {
        return t -> Result.fail(t.getMessage());
    }
}
