package com.blr19c.common.remote.config.load;

import com.blr19c.common.remote.registry.ProtocolEnum;
import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.auth.Authenticator;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用加载程序
 *
 * @author blr
 * @since 2021.4.16
 */
public class UniversalLoader {
    private static Field adaptedInterceptorsField;
    private static final List<RpcURI> serviceRegistrar = new ArrayList<>();
    private static final Map<Class<?>, Object> loadedLocal = new ConcurrentHashMap<>();

    public static <T> T getLoadedLocal(Class<T> type) {
        return (T) loadedLocal.get(type);
    }

    public static <T> T addLoadedLocal(Class<T> type, Object source) {
        return (T) loadedLocal.put(type, source);
    }

    public static Authenticator getAuthenticator() {
        return null;
    }

    /**
     * 注册服务
     */
    public static void addServiceRegistrar(RpcURI rpcURI) {
        synchronized (serviceRegistrar) {
            serviceRegistrar.add(rpcURI);
        }
    }

    /**
     * 卸载服务
     */
    public static void removeServiceRegistrar(RpcURI rpcURI) {
        synchronized (serviceRegistrar) {
            serviceRegistrar.remove(rpcURI);
        }
    }

    /**
     * 获取注册列表
     */
    public static synchronized List<RpcURI> getServiceRegistrarList() {
        synchronized (serviceRegistrar) {
            return new ArrayList<>(serviceRegistrar);
        }
    }

    public static ProtocolEnum getProtocol() {
        return ProtocolEnum.REDIS;
    }


}
