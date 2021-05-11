package com.blr19c.common.remote.rpc.http;

import com.blr19c.common.remote.common.Result;
import com.blr19c.common.remote.common.RpcInvocation;
import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.registry.RemoteRegistryHolder;
import com.blr19c.common.remote.serialization.Serialization;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理
 *
 * @author blr
 * @since 2021.4.24
 */
public class HttpRpcBeanFactory implements InvocationHandler {

    private final Class<?> type;
    private final String serviceName;
    private final Serialization serialization;
    private final String prefixPath;
    private volatile RpcURI rpcURI;
    private WebClient client;

    public HttpRpcBeanFactory(String serviceName, Class<?> type, Serialization serialization, String prefixPath) {
        this.type = type;
        this.serviceName = serviceName;
        this.serialization = serialization;
        this.prefixPath = prefixPath;
    }

    private synchronized void init(String serviceName, Class<?> type, String prefixPath) {
        if (rpcURI != null)
            return;
        this.rpcURI = RemoteRegistryHolder.getRpcURI(serviceName, type);
        this.client = WebClient.builder()
                .baseUrl("http://" + rpcURI.getHost() + ":" + rpcURI.getPort() + prefixPath)
                .build();
    }

    public static Object create(String serviceName, Class<?> type, Serialization serialization, String prefixPath) {
        return Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class[]{type},
                new HttpRpcBeanFactory(serviceName, type, serialization, prefixPath)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        init(serviceName, type, prefixPath);
        RpcInvocation rpcInvocation = new RpcInvocation(
                serviceName,
                type,
                method.getName(),
                method.getParameterTypes(),
                args
        );
        ByteOutputStream outputStream = new ByteOutputStream();
        serialization.serialize(outputStream, rpcInvocation);
        Mono<byte[]> mono = client.post()
                .body(BodyInserters.fromValue(outputStream.getBytes()))
                .retrieve().bodyToMono(byte[].class);
        byte[] block = mono.block();
        if (block == null)
            return null;
        return serialization.deserialize(new ByteArrayInputStream(block), Result.class).getData();
    }
}
