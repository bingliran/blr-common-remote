package com.blr19c.common.remote.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 序列化
 *
 * @author blr
 * @since 2021.4.16
 */
public interface Serialization {

    /**
     * 序列化
     */
    void serialize(OutputStream outputStream, Object obj) throws IOException;

    /**
     * 反序列化
     */
    <T> T deserialize(InputStream inputStream, Class<T> cls) throws IOException;
}
