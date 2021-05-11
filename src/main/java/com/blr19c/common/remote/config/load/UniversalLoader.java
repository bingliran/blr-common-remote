package com.blr19c.common.remote.config.load;

import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.auth.Authenticator;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
     * 获取注册列表
     */
    public static synchronized List<RpcURI> getServiceRegistrarList() {
        synchronized (serviceRegistrar) {
            return new ArrayList<>(serviceRegistrar);
        }
    }
}
