package com.blr19c.common.remote.remoting.inline;

import com.blr19c.common.remote.common.Result;
import com.blr19c.common.remote.common.RpcInvocation;
import com.blr19c.common.remote.common.RpcURI;
import com.blr19c.common.remote.config.auth.Authenticator;
import com.blr19c.common.remote.config.load.UniversalLoader;
import com.blr19c.common.remote.registry.RemoteRegistryHolder;
import com.blr19c.common.remote.remoting.RemotingResponse;
import com.blr19c.common.remote.serialization.Serialization;
import com.blr19c.common.remote.serialization.java.JavaSerialization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 处理调用
 *
 * @author blr
 * @since 2021.4.16
 */
public class InlineRemotingHandler {

    protected final Serialization serialization;

    public InlineRemotingHandler(Serialization serialization) {
        this.serialization = serialization;
    }

    public void invoker(HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RpcInvocation rpcInvocation = serialization.deserialize(request.getInputStream(), RpcInvocation.class);
        //校验
        Authenticator authenticator = UniversalLoader.getAuthenticator();
        if (authenticator != null && !authenticator.auth(rpcInvocation, new InlineRemotingResponse(response)))
            return;
        //本地调用
        RpcURI rpcURI = RemoteRegistryHolder.getRpcURI(rpcInvocation.getServiceName(), rpcInvocation.getType());
        Object target = UniversalLoader.getLoadedLocal(rpcURI.getType());
        Method invokerMethod = rpcURI.getType().getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParameterTypes());
        Object data = invokerMethod.invoke(target, rpcInvocation.getArgs());
        serialization.serialize(response.getOutputStream(), new Result(System.currentTimeMillis(), data, 0));
    }

    static class InlineRemotingResponse implements RemotingResponse {

        HttpServletResponse response;

        InlineRemotingResponse(HttpServletResponse response) {
            this.response = response;
        }

        @Override
        public void addHeader(String name, Object value) {
            response.addHeader(name, String.valueOf(value));
        }

        @Override
        public void removeHeader(String name) {
            response.setHeader(name, "");
        }

        @Override
        public Map<String, Object> headers() {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String headerName : response.getHeaderNames())
                map.put(headerName, response.getHeader(headerName));
            return map;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return response.getOutputStream();
        }
    }
}
