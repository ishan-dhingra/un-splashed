package com.anythingintellect.unsplashed.di;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.unsplashed.network.UnSplashedAPI;
import com.anythingintellect.unsplashed.network.UnSplashedAPIImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

@Module(includes = NetworkLibModule.class)
public class AppModule {

    @Singleton
    @Provides
    public UnSplashedAPI providesUnSplashedAPI(RequestManager requestManager) {
        return new UnSplashedAPIImpl(requestManager);
    }

}
