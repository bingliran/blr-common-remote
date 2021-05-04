package com.blr19c.common.remote.serialization.json;

import com.blr19c.common.remote.serialization.Serialization;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * json Serialization
 *
 * @author blr
 * @since 2021.4.27
 */
public class JsonSerialization implements Serialization {

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void serialize(OutputStream outputStream, Object obj) throws IOException {
        objectMapper.writeValue(outputStream, obj);
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> cls) throws IOException {
        return objectMapper.readValue(inputStream, cls);
    }
}
