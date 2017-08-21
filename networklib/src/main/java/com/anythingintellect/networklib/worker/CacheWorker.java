package com.anythingintellect.networklib.worker;

import com.anythingintellect.networklib.cache.Cache;
import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseHandler;

import org.apache.http.HttpStatus;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class CacheWorker extends Thread implements Worker {

    private final PriorityBlockingQueue<Request<?>> cacheQueue;
    private final PriorityBlockingQueue<Request<?>> networkQueue;
    private final Cache cache;
    private final ResponseHandler responseHandler;
    private volatile boolean quit = false;

    public CacheWorker(PriorityBlockingQueue<Request<?>> cacheQueue, PriorityBlockingQueue<Request<?>> networkQueue, Cache cache, ResponseHandler responseHandler) {
        this.cacheQueue = cacheQueue;
        this.networkQueue = networkQueue;
        this.cache = cache;
        this.responseHandler = responseHandler;
    }

    @Override
    public void run() {
        Request<?> request;
        while (!quit) {
            try {
                request = cacheQueue.take();
            } catch (InterruptedException e) {
                continue;
            }
            if (request.isCanceled()) {
                continue;
            }
            processRequest(request);
        }
    }

    @SuppressWarnings("deprecation")
    private void processRequest(Request<?> request) {
        Cache.CacheEntry cacheEntry = cache.get(request.getKey());
        if (cacheEntry == null) {
            networkQueue.offer(request);
            return;
        }
        // Not preserving headers for now
        RawResponse rawResponse = new RawResponse(HttpStatus.SC_OK, cacheEntry.getData(), null);
        Response<?> response = request.parseResponse(rawResponse);
        responseHandler.deliverResponse(request, response);
    }

    @Override
    public void quit() {
        quit = false;
        interrupt();
    }

}
