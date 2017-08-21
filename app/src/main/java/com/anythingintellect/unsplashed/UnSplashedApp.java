package com.anythingintellect.unsplashed;

import android.app.Application;

import com.anythingintellect.unsplashed.di.AppComponent;
import com.anythingintellect.unsplashed.di.AppModule;
import com.anythingintellect.unsplashed.di.DaggerAppComponent;
import com.anythingintellect.unsplashed.di.NetworkLibModule;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class UnSplashedApp extends Application {

    private AppComponent appComponent;
    // 10mb
    private static final int CACHE_SIZE = 1024 * 1024 * 10;
    // Number of request supported in parallel
    // Best practice is to keep the count equal to number of cpu cores
    // For now just keeping is static
    private static final int CON_REQUEST = 4;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .networkLibModule(new NetworkLibModule(CACHE_SIZE, CON_REQUEST))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
