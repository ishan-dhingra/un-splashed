package com.anythingintellect.networklib.worker;

import com.anythingintellect.networklib.cache.Cache;
import com.anythingintellect.networklib.network.NetworkDriver;
import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;
import com.anythingintellect.networklib.response.ResponseHandler;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class NetworkWorker extends Thread implements Worker {

    private final PriorityBlockingQueue<Request<?>> networkQueue;
    private final NetworkDriver networkDriver;
    private final Cache cache;
    private final ResponseHandler responseHandler;
    private volatile boolean quit = false;

    public NetworkWorker(PriorityBlockingQueue<Request<?>> networkQueue, NetworkDriver networkDriver,
                         Cache cache, ResponseHandler responseHandler) {
        this.networkQueue = networkQueue;
        this.networkDriver = networkDriver;
        this.cache = cache;
        this.responseHandler = responseHandler;
    }

    @Override
    public void run() {
        while (!quit) {
            Request<?> request;
            try {
                request = networkQueue.take();
            } catch (InterruptedException ex) {
                continue;
            }
            if (request.isCanceled()) {
                continue;
            }
            processRequest(request);
        }
    }

    private void processRequest(Request<?> request) {
        try {
            RawResponse rawResponse = networkDriver.doRequest(request);
            Response<?> response = request.parseResponse(rawResponse);
            // Request might have failed in parsing phase
            // to avoid unnecessary check at client redirecting failed to error
            if (response.isSuccess()) {
                addToCache(request, rawResponse);
                responseHandler.deliverResponse(request, response);
            } else {
                responseHandler.deliverError(request, response.getError());
            }

        } catch (ResponseError error) {
            responseHandler.deliverError(request, error);
        }
    }

    private void addToCache(Request<?> request, RawResponse response) {
        cache.put(request.getKey(), new Cache.CacheEntry(response.getData()));
    }

    @Override
    public void quit() {
        quit = true;
        interrupt();
    }
}
