package com.anythingintellect.networklib.response;

import com.anythingintellect.networklib.request.Request;

import java.util.concurrent.Executor;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public interface ResponseHandler {

    void deliverResponse(Request<?> request, Response<?> response);

    void deliverError(Request<?> request, ResponseError error);

}
