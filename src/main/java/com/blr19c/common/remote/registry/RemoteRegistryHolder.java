package com.blr19c.common.remote.registry;

import com.blr19c.common.remote.common.RpcURI;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册信息
 *
 * @author blr
 * @since 2021.5.3
 */
public class RemoteRegistryHolder {
    private static final Map<NameAndType, List<RpcURI>> consumerRpcURI = new ConcurrentHashMap<>();

    public static RpcURI getRpcURI(String serviceName) {
        return getRpcURI(new OnlyName(serviceName)).get(0);
    }

    public static RpcURI getRpcURI(String serviceName, Class<?> type) {
        List<RpcURI> rpcURIS = getRpcURI(new NameAndType(serviceName, type));
        if (rpcURIS == null)
            rpcURIS = getRpcURI(new OnlyType(type));
        if (rpcURIS == null)
            rpcURIS = getRpcURI(new OnlyName(serviceName));
        if (rpcURIS == null || rpcURIS.isEmpty())
            throw new IllegalArgumentException(serviceName + ":" + type.getName() + "may unregistered");
        return rpcURIS.get(0);
    }

    public static RpcURI getRpcURI(Class<?> type) {
        return getRpcURI(new OnlyType(type)).get(0);
    }

    public static void putRpcURI(RpcURI rpcURI) {
        consumerRpcURI.put(new NameAndType(rpcURI.getPath(), rpcURI.getType()), Collections.singletonList(rpcURI));
    }

    public static void remove(RpcURI uri) {
    }

    private static List<RpcURI> getRpcURI(NameAndType nameAndType) {
        return consumerRpcURI.get(nameAndType);
    }


    static class NameAndType {
        protected final String name;
        protected final Class<?> type;

        public NameAndType(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (NameAndType.class.equals(obj.getClass()))
                return this.type.isAssignableFrom(((NameAndType) obj).type) && this.name.equals(((NameAndType) obj).name);
            if (obj instanceof NameAndType)
                return obj.equals(this);
            return super.equals(obj);
        }
    }

    static class OnlyName extends NameAndType {

        public OnlyName(String name) {
            super(name, null);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof NameAndType && ((NameAndType) obj).name.equals(this.name);
        }
    }

    static class OnlyType extends NameAndType {

        public OnlyType(Class<?> type) {
            super(null, type);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof NameAndType && this.type.isAssignableFrom(((NameAndType) obj).type);
        }
    }
}
