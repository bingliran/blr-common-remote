package com.blr19c.common.remote.config.load;

/**
 * RpcReference init
 *
 * @author blr
 * @since 2021.4.24
 */
public interface RpcReferenceBeanInitInterface {

    Object init(String serviceName, Class<?> type);

}
