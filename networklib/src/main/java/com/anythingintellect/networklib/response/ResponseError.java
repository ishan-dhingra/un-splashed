package com.anythingintellect.networklib.response;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class ResponseError extends Exception {

    private final RawResponse rawResponse;

    public ResponseError(String msg, Throwable reason, RawResponse rawResponse) {
        super(msg, reason);
        this.rawResponse = rawResponse;
    }

    public RawResponse getRawResponse() {
        return rawResponse;
    }
}
