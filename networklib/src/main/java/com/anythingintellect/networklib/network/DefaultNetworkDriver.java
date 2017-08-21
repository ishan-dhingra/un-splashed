package com.anythingintellect.networklib.network;

import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.ResponseError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

@SuppressWarnings("deprecation")
public class DefaultNetworkDriver implements NetworkDriver {

    private final HttpDriver httpDriver;

    public DefaultNetworkDriver(HttpDriver httpDriver) {
        this.httpDriver = httpDriver;
    }

    @Override
    public RawResponse doRequest(Request<?> request) throws ResponseError {
        try {
            HttpResponse httpResponse = httpDriver.performRequest(request);
            int status = httpResponse.getStatusLine().getStatusCode();
            byte[] responseBytes = entityToBytes(httpResponse.getEntity());
            return new RawResponse(status, responseBytes, null);
        } catch (IOException e) {
            throw new ResponseError("Network Error", e, null);
        }
    }

    private byte[] entityToBytes(HttpEntity entity)
            throws IOException {

        final InputStream inputStream;
        inputStream = entity.getContent();

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer;
        try {
            InputStream in = inputStream;
            if (in == null)
                throw new IOException();
            buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            try {
                // Close the InputStream and release the resources by
                // "consuming the content".
                entity.consumeContent();
                inputStream.close();
            } catch (IOException e) {
                bytes.close();
                throw e;
            }
            bytes.close();
        }
    }
}
