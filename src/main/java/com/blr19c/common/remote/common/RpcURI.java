package com.blr19c.common.remote.common;

import com.blr19c.common.remote.registry.ProtocolEnum;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路径资源
 *
 * @author blr
 * @since 2021.4.16
 */
public class RpcURI {
    protected final URI uri;
    protected ProtocolEnum protocol;
    protected String username;
    protected String password;
    protected String host;
    protected int port;
    protected String path;
    protected Class<?> type;
    protected Map<String, String> paramArgs = new ConcurrentHashMap<>();

    public RpcURI(URI uri) {
        this.uri = uri;
        this.protocol = ProtocolEnum.value(uri.getScheme());
        String[] userInfo = uri.getUserInfo().split(":");
        if (userInfo.length > 2)
            throw new IllegalArgumentException("userInfo has other values besides username and password");
        if (userInfo.length != 0) {
            this.username = userInfo[0];
            this.password = userInfo[1 & userInfo.length - 1];//always 0 or 1
        }
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.path = path.startsWith("/") ? path.substring(Math.min(1, path.length() - 1)) : path;
        String query = uri.getQuery();
        if (StringUtils.isNotEmpty(query)) {
            Arrays.stream(query.split("&"))
                    .filter(StringUtils::isNotEmpty)
                    .map(param -> param.split("=", 2))
                    .filter(pr -> pr.length == 2)
                    .forEach(pr -> paramArgs.put(pr[0], pr[1]));
        }
    }

    public static RpcURI create(String str) {
        try {
            RpcURI rpcURI = new RpcURI(new URI(str));
            String type = rpcURI.getParamArgs("type");
            if (type != null)
                rpcURI.setType(Thread.currentThread().getContextClassLoader().loadClass(type));
            return rpcURI;
        } catch (URISyntaxException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RpcURI create(String str, Class<?> type) {
        RpcURI rpcURI = create(str);
        rpcURI.setType(type);
        return rpcURI;
    }

    public URI getUri() {
        return uri;
    }

    public ProtocolEnum getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolEnum protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
        this.setParamArgs("type", type.getName());
    }

    public String getParamArgs(String key) {
        return paramArgs.get(key);
    }

    public void setParamArgs(String key, String value) {
        paramArgs.put(key, value);
    }

    @Override
    public String toString() {
        if (paramArgs.isEmpty())
            return uri.toString();
        StringJoiner sb = new StringJoiner("&", "?", "");
        for (Map.Entry<String, String> entry : paramArgs.entrySet()) {
            sb.add(entry.getKey() + "=" + entry.getValue());
        }
        return uri.toString() + sb.toString();
    }
}
