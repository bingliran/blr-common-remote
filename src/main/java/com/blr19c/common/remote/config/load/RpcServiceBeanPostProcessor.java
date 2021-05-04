package com.blr19c.common.remote.config.load;

import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.registry.redis.RedisRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.MultiValueMap;

import java.net.*;
import java.util.*;

/**
 * 注册服务
 *
 * @author blr
 * @since 2021.4.23
 */
public class RpcServiceBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    private final String address;

    public RpcServiceBeanPostProcessor(Environment environment) throws SocketException, UnknownHostException {
        String port = environment.getProperty("server.port");
        this.address = getIp() + ":" + port;
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
        return UniversalLoader.getProtocol() + "://@" + address + "/" + serviceName;
    }

    public static String getIp() throws SocketException, UnknownHostException {
        List<String> hostList = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaceList = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaceList.hasMoreElements()) {
            NetworkInterface netInterface = networkInterfaceList.nextElement();
            // 去除回环接口，子接口，未运行和接口
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp())
                continue;
            Enumeration<InetAddress> addressList = netInterface.getInetAddresses();
            while (addressList.hasMoreElements()) {
                InetAddress ip = addressList.nextElement();
                if (ip instanceof Inet4Address) {
                    hostList.add(ip.getHostAddress());
                }
            }
        }
        //最早注册的有效网卡
        return hostList.isEmpty() ? InetAddress.getLocalHost().getHostAddress() : hostList.get(hostList.size() - 1);
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        System.out.println(getIp());
    }
}
