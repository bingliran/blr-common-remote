package com.blr19c.common.remote.rpc.api;

/**
 * 连接信息
 *
 * @author blr
 */
public interface ConnectionNode extends AutoCloseable {

    /**
     * url信息
     */
    //URL getURL();

    /**
     * 能使用 ?
     */
    boolean isAvailable();

    /**
     * 销毁
     */
    void destroy() throws Exception;

    /**
     * 使用 try(? extends AutoCloseable = ...){...}
     */
    @Override
    default void close() throws Exception {
        destroy();
    }
}
