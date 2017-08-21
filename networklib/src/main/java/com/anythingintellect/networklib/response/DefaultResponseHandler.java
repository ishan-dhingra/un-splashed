package com.anythingintellect.networklib.response;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.anythingintellect.networklib.request.Request;

import java.util.concurrent.Executor;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class DefaultResponseHandler implements ResponseHandler {

    private final Executor executor;

    // Pass handler of the thread on which you want the response to be delivered
    // It's main thread for Android
    public DefaultResponseHandler(final Handler handler) {
        this.executor = new Executor() {
            @Override
            public void execute(@NonNull Runnable runnable) {
                handler.post(runnable);
            }
        };
    }



    @Override
    public void deliverResponse(Request<?> request, Response<?> response) {
        executor.execute(new ResponseHandlerRunnable(request, response));
    }

    @Override
    public void deliverError(Request<?> request, ResponseError error) {
        executor.execute(new ResponseHandlerRunnable(request, error));
    }

    private class ResponseHandlerRunnable implements Runnable {
        private final Request request;
        private final Response response;
        private final ResponseError error;

        private ResponseHandlerRunnable(Request<?> request, Response<?> response) {
            this.request = request;
            this.response = response;
            this.error = null;
        }
        private ResponseHandlerRunnable(Request<?> request, ResponseError error) {
            this.request = request;
            this.response = null;
            this.error = error;
        }


        @Override
        public void run() {
            if (request.isCanceled()) {
                request.finish();
                return;
            }

            if (response != null) {
                request.deliverResponse(response.getResponse());
            } else {
                request.deliverError(error);
            }

            request.finish();
        }
    }
}
