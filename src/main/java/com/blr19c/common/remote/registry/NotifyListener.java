package com.blr19c.common.remote.registry;

import com.blr19c.common.remote.common.RpcURI;

import java.util.List;

/**
 * 注册唤醒
 *
 * @author blr
 * @since 2021.4.22
 */
public interface NotifyListener {

    void notify(List<RpcURI> uriList);
}
