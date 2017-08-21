package com.anythingintellect.networklib.network;

import android.text.TextUtils;

import com.anythingintellect.networklib.request.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static okhttp3.Protocol.HTTP_1_0;
import static okhttp3.Protocol.HTTP_1_1;
import static okhttp3.Protocol.HTTP_2;
import static okhttp3.Protocol.SPDY_3;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

@SuppressWarnings("deprecation")
public class DefaultHttpDriver implements HttpDriver {


    private final OkHttpClient httpClient;

    public DefaultHttpDriver(OkHttpClient client) {
        this.httpClient = client;
    }

    public HttpResponse performRequest(Request<?> request) throws IOException {

        okhttp3.Request.Builder okHttpRequestBuilder = new okhttp3.Request.Builder();
        okHttpRequestBuilder.url(request.getUrl());

        appendRequestType(okHttpRequestBuilder, request);


        okhttp3.Request okHttpRequest = okHttpRequestBuilder
                .build();
        Call okHttpCall = httpClient.newCall(okHttpRequest);
        Response okHttpResponse = okHttpCall.execute();

        StatusLine responseStatus = new BasicStatusLine(
                parseProtocol(okHttpResponse.protocol()),
                okHttpResponse.code(), okHttpResponse.message());

        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        response.setEntity(entityFromOkHttpResponse(okHttpResponse));

        Headers responseHeaders = okHttpResponse.headers();

        for (int i = 0, len = responseHeaders.size(); i < len; i++) {
            final String name = responseHeaders.name(i), value = responseHeaders
                    .value(i);

            if (name != null) {
                response.addHeader(new BasicHeader(name, value));
            }
        }

        return response;
    }

    private void appendRequestType(okhttp3.Request.Builder okHttpRequestBuilder, Request<?> request) {
        if(request.getMethod() == Request.Method.GET) {
            okHttpRequestBuilder.get();
        } else if (request.getMethod() == Request.Method.POST){
            okHttpRequestBuilder.post(RequestBody.create(MediaType.parse(request.getContentType()),
                    request.getBody()));
        }
    }

    private static HttpEntity entityFromOkHttpResponse(Response response)
            throws IOException {
        BasicHttpEntity entity = new BasicHttpEntity();
        ResponseBody body = response.body();

        entity.setContent(body.byteStream());
        entity.setContentLength(body.contentLength());
        entity.setContentEncoding(response.header("Content-Encoding"));

        if (body.contentType() != null) {
            entity.setContentType(body.contentType().type());
        }
        return entity;
    }

    private static ProtocolVersion parseProtocol(final Protocol protocol) {
        switch (protocol) {
            case HTTP_1_0 :
                return new ProtocolVersion("HTTP", 1, 0);
            case HTTP_1_1 :
                return new ProtocolVersion("HTTP", 1, 1);
            case SPDY_3 :
                return new ProtocolVersion("SPDY", 3, 1);
            case HTTP_2 :
                return new ProtocolVersion("HTTP", 2, 0);
        }

        throw new IllegalAccessError("Unkwown protocol");
    }

}
