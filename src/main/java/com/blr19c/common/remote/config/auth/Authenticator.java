package com.blr19c.common.remote.config.auth;

import com.blr19c.common.remote.common.RpcInvocation;
import com.blr19c.common.remote.remoting.RemotingResponse;

/**
 * 认证接口
 *
 * @author blr
 */
public interface Authenticator {
    /**
     * 权限校验
     *
     * @param rpcInvocation    请求信息
     * @param remotingResponse response
     * @return false 结束
     */
    boolean auth(RpcInvocation rpcInvocation, RemotingResponse remotingResponse);

}
