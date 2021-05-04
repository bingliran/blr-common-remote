package com.blr19c.common.remote.config;

import com.blr19c.common.remote.config.load.RpcReferenceAnnotationBeanPostProcessor;
import com.blr19c.common.remote.config.load.RpcReferenceBeanInitInterface;
import com.blr19c.common.remote.config.load.RpcServiceBeanPostProcessor;
import com.blr19c.common.remote.registry.RemoteRegistry;
import com.blr19c.common.remote.registry.redis.RedisRegistry;
import com.blr19c.common.remote.remoting.RemotingServer;
import com.blr19c.common.remote.remoting.inline.InlineRemotingDispatcherServlet;
import com.blr19c.common.remote.rpc.inline.InlineRpcBeanInit;
import com.blr19c.common.remote.serialization.java.JavaSerialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.SocketException;
import java.net.UnknownHostException;

@Configuration
public class RemoteAutoConfig {

    @Bean
    public RemoteRegistry remoteRegistry(StringRedisTemplate redisTemplate) {
        RemoteRegistry remoteRegistry = new RedisRegistry(redisTemplate);
        remoteRegistry.init();
        return remoteRegistry;
    }

    @Bean
    public RemotingServer remotingServer(RequestMappingHandlerMapping handlerMapping) throws Exception {
        return new InlineRemotingDispatcherServlet(handlerMapping, new JavaSerialization()).start();
    }

    @Bean
    public RpcReferenceAnnotationBeanPostProcessor rpcReferenceAnnotationBeanPostProcessor(RpcReferenceBeanInitInterface init) {
        return new RpcReferenceAnnotationBeanPostProcessor(init);
    }

    @Bean
    public RpcReferenceBeanInitInterface rpcReferenceBeanInitInterface() {
        return new InlineRpcBeanInit();
    }

    @Bean
    public RpcServiceBeanPostProcessor rpcServiceBeanPostProcessor(Environment environment) throws SocketException, UnknownHostException {
        return new RpcServiceBeanPostProcessor(environment);
    }
}
