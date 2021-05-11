package com.blr19c.common.remote.config;

import com.blr19c.common.remote.config.load.*;
import com.blr19c.common.remote.registry.ProtocolEnum;
import com.blr19c.common.remote.registry.RemoteRegistry;
import com.blr19c.common.remote.registry.redis.RedisRegistry;
import com.blr19c.common.remote.remoting.RemotingServer;
import com.blr19c.common.remote.remoting.inline.InlineRemotingDispatcherServlet;
import com.blr19c.common.remote.remoting.netty.NettyServer;
import com.blr19c.common.remote.rpc.http.HttpRpcBeanInit;
import com.blr19c.common.remote.serialization.Serialization;
import com.blr19c.common.remote.serialization.avro.AvroSerialization;
import com.blr19c.common.remote.serialization.fst.FstSerialization;
import com.blr19c.common.remote.serialization.java.JavaSerialization;
import com.blr19c.common.remote.serialization.json.JsonSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Objects;

/**
 * remote配置
 *
 * @author blr
 * @since 2021.5.11
 */
@Configuration
@ConditionalOnBean(EnableRpcObserver.class)
@EnableConfigurationProperties(RemoteConfigProperties.class)
public class RemoteAutoConfig {
    private final RemoteConfigProperties remoteConfigProperties;

    @Autowired(required = false)
    public RemoteAutoConfig(RemoteConfigProperties remoteConfigProperties,
                            EnableRpcObserver enableRpcObserver) {
        this.remoteConfigProperties = Objects.requireNonNull(remoteConfigProperties);
        EnableRpc annotation = Objects.requireNonNull(enableRpcObserver.getAnnotation());
        if (this.remoteConfigProperties.getRemote() == null) {
            this.remoteConfigProperties.setRemote(annotation.remoting());
        }
        if (this.remoteConfigProperties.getRequest() == null) {
            this.remoteConfigProperties.setRequest(annotation.request());
        }
        if (this.remoteConfigProperties.getRegistry() == null) {
            this.remoteConfigProperties.setRegistry(annotation.protocol());
        }
        if (this.remoteConfigProperties.getSerialization() == null) {
            this.remoteConfigProperties.setSerialization(annotation.serialization());
        }
    }

    @Bean
    @ConditionalOnMissingBean(RemoteRegistry.class)
    @Autowired(required = false)
    public RemoteRegistry remoteRegistry(StringRedisTemplate redisTemplate) {
        if (ProtocolEnum.REDIS == remoteConfigProperties.getRegistry() && redisTemplate == null)
            throw new IllegalArgumentException("redis protocol RedisTemplate must exist!");
        //curr only redis
        RemoteRegistry remoteRegistry = new RedisRegistry(redisTemplate);
        remoteRegistry.init();
        return remoteRegistry;
    }

    @Bean
    @ConditionalOnMissingBean(RemotingServer.class)
    @ConditionalOnProperty(prefix = "remote", name = "onlyRequest", havingValue = "false", matchIfMissing = true)
    @Autowired(required = false)
    public RemotingServer remotingServer(Serialization serialization, RequestMappingHandlerMapping handlerMapping) throws Exception {
        Objects.requireNonNull(serialization);
        switch (remoteConfigProperties.getRemote()) {
            case INLINE:
                if (handlerMapping == null)
                    throw new IllegalArgumentException("inline remoting RequestMappingHandlerMapping must exist!");
                return new InlineRemotingDispatcherServlet(handlerMapping, serialization, remoteConfigProperties).start();
            case NETTY:
                return new NettyServer(serialization, remoteConfigProperties).start();
        }
        //FIXME: Should an error message be prompted
        return new NettyServer(serialization, remoteConfigProperties).start();
    }

    @Bean
    @ConditionalOnMissingBean(Serialization.class)
    public Serialization serialization() {
        switch (remoteConfigProperties.getSerialization()) {
            case FST:
                return new FstSerialization();
            case AVRO:
                return new AvroSerialization();
            case JAVA:
                return new JavaSerialization();
            case JSON:
                return new JsonSerialization();
            case OTHER:
            default:
                throw new IllegalArgumentException("You should override this method when the type is other!");
        }
    }

    @Bean
    public RpcReferenceBeanInitInterface rpcReferenceBeanInitInterface(Serialization serialization) {
        return new HttpRpcBeanInit(serialization, remoteConfigProperties);
    }

    //bean post processor

    @Bean
    public RpcReferenceAnnotationBeanPostProcessor rpcReferenceAnnotationBeanPostProcessor(RpcReferenceBeanInitInterface rpcReferenceBeanInitInterface) {
        return new RpcReferenceAnnotationBeanPostProcessor(rpcReferenceBeanInitInterface);
    }

    @Bean
    @ConditionalOnProperty(prefix = "remote", name = "onlyRequest", havingValue = "false", matchIfMissing = true)
    public RpcServiceBeanPostProcessor rpcServiceBeanPostProcessor(Environment environment) {
        return new RpcServiceBeanPostProcessor(environment, remoteConfigProperties);
    }
}
