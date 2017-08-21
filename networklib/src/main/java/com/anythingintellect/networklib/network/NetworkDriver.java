package com.anythingintellect.networklib.network;

import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.ResponseError;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public interface NetworkDriver {

    RawResponse doRequest(Request<?> request) throws ResponseError;

}
