package com.blr19c.common.remote.remoting.inline;

import com.blr19c.common.remote.common.Result;
import com.blr19c.common.remote.remoting.AbstractRemotingHandler;
import com.blr19c.common.remote.serialization.Serialization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理调用
 *
 * @author blr
 * @since 2021.4.16
 */
public class InlineRemotingHandler extends AbstractRemotingHandler {

    public InlineRemotingHandler(Serialization serialization) {
        super(serialization);
    }

    public void invoker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result invocation = super.invocation(request.getInputStream());
        serialization.serialize(response.getOutputStream(), invocation);
    }

    /*static class InlineRemotingResponse implements RemotingResponse {

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
    }*/
}
