package com.blr19c.common.remote.config.load;

import com.blr19c.common.remote.registry.ProtocolEnum;
import com.blr19c.common.remote.remoting.RemotingEnum;
import com.blr19c.common.remote.rpc.RpcRequestEnum;
import com.blr19c.common.remote.serialization.SerializationEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用
 *
 * @author blr
 * @since 2021.4.22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableRpcRegistrar.class)
public @interface EnableRpc {

    /**
     * 注册中心
     */
    ProtocolEnum protocol() default ProtocolEnum.REDIS;

    /**
     * 序列化方式
     */
    SerializationEnum serialization() default SerializationEnum.JAVA;

    /**
     * 访问方式
     */
    RemotingEnum remoting() default RemotingEnum.INLINE;

    /**
     * 请求方式
     */
    RpcRequestEnum request() default RpcRequestEnum.HTTP;
}
