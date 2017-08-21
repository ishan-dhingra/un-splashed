package com.anythingintellect.networklib.request;

import android.support.annotation.NonNull;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;

import java.util.Map;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public abstract class Request<T> implements Comparable<Request<T>> {

    private final String url;
    private final Method method;
    private final Response.Listener<T> listener;
    private Integer sequence;
    private boolean isCanceled;
    private String contentType;
    private RequestManager requestManager;
    private boolean skipCache;

    public Request(String url, Method method, Response.Listener<T> listener) {
        this.url = url;
        this.method = method;
        this.listener = listener;
    }


    public String getUrl() {
        return url;
    }

    public Method getMethod() {
        return method;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void cancel() {
        isCanceled = true;
    }

    public Response.Listener<T> getListener() {
        return listener;
    }

    public byte[] getBody() {
        return new byte[0];
    }

    public void deliverResponse(T response) {
        listener.onSuccess(response);
    }

    public void deliverError(ResponseError error) {
        listener.onError(error);
    }

    // For FIFO processing
    @Override
    public int compareTo(@NonNull Request<T> request) {
        return this.sequence - request.sequence;
    }

    // For Client

    public abstract Response<T> parseResponse(RawResponse rawResponse);

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public void finish() {
        requestManager.finish(this);
    }

    public boolean isSkipCache() {
        return skipCache;
    }

    public void setSkipCache(boolean skipCache) {
        this.skipCache = skipCache;
    }


    //  Taking only two methods as support to keep it focused
    public enum Method {
        GET,
        POST
    }

    @Override
    public String toString() {
        return "Request: " + getUrl() + " Sequence: " + getSequence() + " Is Canceled: " + isCanceled;
    }


    // Just assuming URL for only for request uniqueness
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Request)) {
            return false;
        }
        return ((Request)obj).getUrl().equalsIgnoreCase(getUrl());
    }

    @Override
    public int hashCode() {
        return getUrl().hashCode();
    }

    // For now url is used as key
    public String getKey() {
        return getUrl();
    }
}
