package com.blr19c.common.remote.serialization.avro;

import com.blr19c.common.remote.serialization.Serialization;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * avro Serialization
 *
 * @author blr
 * @since 2021.4.27
 */
public class AvroSerialization implements Serialization {

    @Override
    public void serialize(OutputStream outputStream, Object obj) throws IOException {
        DatumWriter<Object> datumWriter = new SpecificDatumWriter<>(Object.class);
        DataFileWriter<Object> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(Schema.create(Schema.Type.NULL), outputStream);
        dataFileWriter.append(obj);
        dataFileWriter.flush();
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> cls) throws IOException {
        DatumReader<T> datumReader = new SpecificDatumReader<>(cls);
        SeekableByteArrayInput seekableByteArrayInput = new SeekableByteArrayInput(IOUtils.toByteArray(inputStream));
        DataFileReader<T> dataFileReader = new DataFileReader<T>(seekableByteArrayInput, datumReader);
        return dataFileReader.hasNext() ? dataFileReader.next() : null;
    }
}
