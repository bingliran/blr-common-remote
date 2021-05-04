package com.blr19c.common.remote.remoting.inline;

import com.blr19c.common.remote.common.Result;
import com.blr19c.common.remote.remoting.RemotingServer;
import com.blr19c.common.remote.serialization.Serialization;
import org.jooq.lambda.Seq;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * inline 远程服务
 *
 * @author blr
 * @since 2021.4.16
 */
public class InlineRemotingDispatcherServlet implements RemotingServer {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final InlineRemotingHandler invoker;
    private final Serialization serialization;

    public InlineRemotingDispatcherServlet(RequestMappingHandlerMapping requestMappingHandlerMapping, Serialization serialization) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.invoker = new InlineRemotingHandler(serialization);
        this.serialization = serialization;
    }

    @Override
    public RemotingServer start() throws Exception {
        //添加拦截器
        RequestMappingInfo mappingInfo = RequestMappingInfo.paths("/remotingServer")
                .methods(RequestMethod.GET, RequestMethod.POST)
                .build();
        Method remotingServer = this.getClass().getMethod("remotingServer", HttpServletRequest.class, HttpServletResponse.class);
        requestMappingHandlerMapping.registerMapping(mappingInfo, this, remotingServer);
        return this;
    }

    public void remotingServer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            invoker.invoker(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.reset();
            serialization.serialize(response.getOutputStream(), Result.fail(e.getMessage()).toBytes());
        }
    }
}
