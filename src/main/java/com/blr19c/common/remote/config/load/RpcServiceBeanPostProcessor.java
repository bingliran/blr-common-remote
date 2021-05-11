package com.blr19c.common.remote.config.load;

import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.RemoteConfigProperties;
import com.blr19c.common.remote.registry.ProtocolEnum;
import com.blr19c.common.remote.remoting.RemotingEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

/**
 * 注册服务
 *
 * @author blr
 * @since 2021.4.23
 */
public class RpcServiceBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    private final String address;
    private final ProtocolEnum protocol;

    public RpcServiceBeanPostProcessor(Environment environment, RemoteConfigProperties properties) {
        String port = properties.getRemote() == RemotingEnum.INLINE ? environment.getProperty("server.port") : String.valueOf(properties.getPort());
        this.address = properties.getIp() + ":" + port;
        this.protocol = properties.getRegistry();
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        RpcService rpcService = AnnotationUtils.findAnnotation(bean.getClass(), RpcService.class);
        if (rpcService == null)
            return true;
        //注册到spring之后缓存到本地
        RpcURI uri = RpcURI.create(getAddress(beanName), bean.getClass().getInterfaces()[0]);
        UniversalLoader.addServiceRegistrar(uri);
        UniversalLoader.addLoadedLocal(uri.getType(), bean);
        return true;
    }

    private String getAddress(String serviceName) {
        return protocol + "://@" + address + "/" + serviceName;
    }
}
