package com.anythingintellect.networklib.network;

import com.anythingintellect.networklib.request.Request;

import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public interface HttpDriver {


    HttpResponse performRequest(Request<?> request) throws IOException;

}
