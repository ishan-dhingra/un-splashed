package com.anythingintellect.networklib.response;

import java.util.Map;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class RawResponse {

    private final int statusCode;

    private final byte[] data;

    private final Map<String, String> headers;


    public RawResponse(int statusCode, byte[] data, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getData() {
        return data;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
