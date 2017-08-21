package com.anythingintellect.networklib.response;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class Response<T> {


    private final T response;
    private final ResponseError error;

    public Response(T response) {
        this.response = response;
        this.error = null;
    }

    public Response(ResponseError error) {
        this.error = error;
        this.response = null;
    }

    public static <T> Response<T> success(T response) {
        return new Response<T>(response);
    }

    public static <T> Response<T> error(ResponseError error) {
        return new Response<>(error);
    }

    public T getResponse() {
        return response;
    }

    public ResponseError getError() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    public interface Listener<T> {
        void onSuccess(T response);
        void onError(ResponseError error);
    }
}
