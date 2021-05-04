package com.blr19c.common.remote.serialization.fst;

import com.blr19c.common.remote.serialization.Serialization;
import org.apache.commons.io.IOUtils;
import org.nustaq.serialization.FSTConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * fst Serialization
 *
 * @author blr
 * @since 2021.4.27
 */
public class FstSerialization implements Serialization {
    protected FSTConfiguration fstConfiguration = FSTConfiguration.getDefaultConfiguration();


    @Override
    public void serialize(OutputStream outputStream, Object obj) throws IOException {
        outputStream.write(fstConfiguration.asByteArray(obj));
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> cls) throws IOException {
        return cls.cast(fstConfiguration.asObject(IOUtils.toByteArray(inputStream)));
    }
}
