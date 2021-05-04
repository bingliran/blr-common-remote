package com.blr19c.common.remote.serialization.java;


import com.blr19c.common.remote.serialization.Serialization;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.*;

/**
 * java Serialization
 *
 * @author blr
 * @since 2021.4.26
 */
public class JavaSerialization implements Serialization {

    @Override
    public void serialize(OutputStream outputStream, Object obj) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new CheckedOutputStream(outputStream, new CRC32()));
        out.putNextEntry(new ZipEntry("1"));
        out.write(getBytes(obj));
        out.finish();
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> cls) throws IOException {
        try {
            ZipInputStream in = new ZipInputStream(new CheckedInputStream(inputStream, new CRC32()));
            if (in.getNextEntry() == null)
                return null;
            return getObject(IOUtils.toByteArray(in), cls);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    protected <T> T getObject(byte[] source, Class<T> cls) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(source))) {
            return cls.cast(objectInputStream.readObject());
        }
    }

    protected byte[] getBytes(Object obj) throws IOException {
        ByteArrayOutputStream fn;
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fn = new ByteArrayOutputStream())) {
            objectOutputStream.writeObject(obj);
        }
        return fn.toByteArray();
    }
}
