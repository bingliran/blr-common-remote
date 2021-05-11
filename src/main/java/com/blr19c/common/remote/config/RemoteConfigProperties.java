package com.blr19c.common.remote.config;

import com.blr19c.common.remote.common.utils.IPUtil;
import com.blr19c.common.remote.registry.ProtocolEnum;
import com.blr19c.common.remote.remoting.RemotingEnum;
import com.blr19c.common.remote.rpc.RpcRequestEnum;
import com.blr19c.common.remote.serialization.SerializationEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("remote")
public class RemoteConfigProperties {

    /**
     * 本机ip
     */
    private String ip = IPUtil.getIp();

    /**
     * 本机服务启动占用端口(inline模式不占用端口)
     */
    private int port = 7888;

    /**
     * 注册中心
     */
    private ProtocolEnum registry;

    /**
     * 服务处理类型
     */
    private RemotingEnum remote;

    /**
     * 请求处理类型
     */
    private RpcRequestEnum request;

    /**
     * 序列化方式
     */
    private SerializationEnum serialization;

    /**
     * inline方式请求/服务映射前缀
     */
    private String inlineMapping = "/remotingServer";

    /**
     * 仅请求不作为服务器
     */
    private boolean onlyRequest;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ProtocolEnum getRegistry() {
        return registry;
    }

    public void setRegistry(ProtocolEnum registry) {
        this.registry = registry;
    }

    public RemotingEnum getRemote() {
        return remote;
    }

    public void setRemote(RemotingEnum remote) {
        this.remote = remote;
    }

    public RpcRequestEnum getRequest() {
        return request;
    }

    public void setRequest(RpcRequestEnum request) {
        this.request = request;
    }

    public SerializationEnum getSerialization() {
        return serialization;
    }

    public void setSerialization(SerializationEnum serialization) {
        this.serialization = serialization;
    }

    public String getInlineMapping() {
        return inlineMapping;
    }

    public void setInlineMapping(String inlineMapping) {
        if (inlineMapping == null) {
            this.inlineMapping = "";
            return;
        }
        this.inlineMapping = inlineMapping.startsWith("/") ? inlineMapping : "/" + inlineMapping;
    }

    public boolean isOnlyRequest() {
        return onlyRequest;
    }

    public void setOnlyRequest(boolean onlyRequest) {
        this.onlyRequest = onlyRequest;
    }
}
