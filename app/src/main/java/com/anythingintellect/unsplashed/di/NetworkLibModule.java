package com.anythingintellect.unsplashed.di;

import android.os.Handler;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.networklib.cache.Cache;
import com.anythingintellect.networklib.cache.InMemoryCache;
import com.anythingintellect.networklib.network.DefaultHttpDriver;
import com.anythingintellect.networklib.network.DefaultNetworkDriver;
import com.anythingintellect.networklib.network.HttpDriver;
import com.anythingintellect.networklib.network.NetworkDriver;
import com.anythingintellect.networklib.response.DefaultResponseHandler;
import com.anythingintellect.networklib.response.ResponseHandler;
import com.anythingintellect.unsplashed.network.UnSplashedAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

@Module
public class NetworkLibModule {

    private final int cacheSize;
    private final int conRequest;

    public NetworkLibModule(int cacheSize, int conRequest) {
        this.cacheSize = cacheSize;
        this.conRequest = conRequest;
    }

    @Provides
    @Singleton
    public Cache providesInMemoryCache() {
        Cache cache = new InMemoryCache();
        cache.init(cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    public HttpDriver providesHttpDriver(OkHttpClient okHttpClient) {
        return new DefaultHttpDriver(okHttpClient);
    }

    @Provides
    @Singleton
    public NetworkDriver providesNetworkDriver(HttpDriver httpDriver) {
        return new DefaultNetworkDriver(httpDriver);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    public ResponseHandler providesResponseHandler(Handler handler) {
        return new DefaultResponseHandler(handler);
    }

    @Provides
    @Singleton
    public Handler providesHandler() {
        return new Handler();
    }

    @Provides
    @Singleton
    public RequestManager providesRequestManager(Cache cache, NetworkDriver networkDriver, ResponseHandler responseHandler) {
        RequestManager requestManager = new RequestManager(cache, networkDriver, conRequest, responseHandler);
        requestManager.start();
        return requestManager;
    }


}
