package com.blr19c.common.remote.rpc.inline;

import com.blr19c.common.remote.config.load.RpcReferenceBeanInitInterface;

/**
 * 初始化远程调用
 *
 * @author blr
 * @since 2021.4.24
 */
public class InlineRpcBeanInit implements RpcReferenceBeanInitInterface {

    @Override
    public Object init(String serviceName, Class<?> type) {
        return InlineRpcBeanFactory.create(serviceName, type);
    }
}
