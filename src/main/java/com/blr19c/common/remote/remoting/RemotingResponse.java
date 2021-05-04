package com.blr19c.common.remote.remoting;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * RemotingResponse
 *
 * @author blr
 * @since 2021.4.16
 */
public interface RemotingResponse {

    void addHeader(String name, Object value);

    void removeHeader(String name);

    Map<String, Object> headers();

    OutputStream getOutputStream() throws IOException;

}
