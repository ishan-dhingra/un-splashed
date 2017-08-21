package com.anythingintellect.networklib;

import com.anythingintellect.networklib.cache.Cache;
import com.anythingintellect.networklib.network.NetworkDriver;
import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.ResponseHandler;
import com.anythingintellect.networklib.worker.CacheWorker;
import com.anythingintellect.networklib.worker.NetworkWorker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class RequestManager {

    private final AtomicInteger sequenceGenerator = new AtomicInteger();
    // Request waiting because of similar request already in flight
    private final Map<String, Queue<Request<?>>> duplicateRequests = new HashMap<>();
    // Request in progress
    private final Set<Request<?>> inFlight = new HashSet<>();

    // Worker shared queues
    private final PriorityBlockingQueue<Request<?>> cacheQueue = new PriorityBlockingQueue<>();
    private final PriorityBlockingQueue<Request<?>> networkQueue = new PriorityBlockingQueue<>();

    private final Cache cache;
    private final NetworkDriver networkDriver;

    private final ResponseHandler responseHandler;

    private final NetworkWorker[] networkWorkers;
    private CacheWorker cacheWorker;



    public RequestManager(Cache cache, NetworkDriver networkDriver, int conRequestCount, ResponseHandler responseHandler) {
        this.cache = cache;
        this.networkDriver = networkDriver;
        this.networkWorkers = new NetworkWorker[conRequestCount];
        this.responseHandler = responseHandler;
    }

    public void addRequest(Request<?> request) {
        request.setRequestManager(this);
        if (request.isSkipCache()) {
            cache.remove(request.getKey());
        }
        synchronized (inFlight) {
            inFlight.add(request);
        }

        request.setSequence(sequenceGenerator.get());
        synchronized (duplicateRequests) {
            if (duplicateRequests.containsKey(request.getKey())) {
                // Request already in flight
                Queue<Request<?>> waitingDuplicates = duplicateRequests.get(request.getKey());
                if (waitingDuplicates == null) {
                    waitingDuplicates = new LinkedList<>();
                }
                // Add this request as listener for request already in flight
                waitingDuplicates.add(request);
                duplicateRequests.put(request.getKey(), waitingDuplicates);
            } else {
                // Request not in progress, add for cache processing
                // and add it to waiting queue
                duplicateRequests.put(request.getKey(), null);
                cacheQueue.add(request);
            }
        }
    }

    public void start() {
        stop();
        cacheWorker = new CacheWorker(cacheQueue, networkQueue, cache, responseHandler);
        cacheWorker.start();
        for (NetworkWorker networkWorker : networkWorkers) {
            networkWorker = new NetworkWorker(networkQueue, networkDriver, cache, responseHandler);
            networkWorker.start();
        }
    }

    public void stop() {
        if (cacheWorker != null) {
            cacheWorker.quit();
        }
        for (NetworkWorker networkWorker : networkWorkers) {
            if (networkWorker != null) {
                networkWorker.quit();
            }
        }
    }

    public void finish(Request<?> orignalRequest) {
        synchronized (inFlight) {
            inFlight.remove(orignalRequest);
        }

        synchronized (duplicateRequests) {
            Queue<Request<?>> waitingRequests = duplicateRequests.remove(orignalRequest.getKey());
            if (waitingRequests != null) {
                for (Request<?> waitingRequest : waitingRequests) {
                    cacheQueue.offer(waitingRequest);
                }
            }
        }
    }


}
