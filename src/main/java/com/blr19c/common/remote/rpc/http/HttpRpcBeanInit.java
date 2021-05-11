package com.blr19c.common.remote.rpc.http;

import com.blr19c.common.remote.config.RemoteConfigProperties;
import com.blr19c.common.remote.config.load.RpcReferenceBeanInitInterface;
import com.blr19c.common.remote.serialization.Serialization;

/**
 * 初始化远程调用
 *
 * @author blr
 * @since 2021.4.24
 */
public class HttpRpcBeanInit implements RpcReferenceBeanInitInterface {
    private final Serialization serialization;
    private final RemoteConfigProperties properties;

    public HttpRpcBeanInit(Serialization serialization, RemoteConfigProperties properties) {
        this.serialization = serialization;
        this.properties = properties;
    }

    @Override
    public Object init(String serviceName, Class<?> type) {
        return HttpRpcBeanFactory.create(serviceName, type, serialization, properties.getInlineMapping());
    }
}
