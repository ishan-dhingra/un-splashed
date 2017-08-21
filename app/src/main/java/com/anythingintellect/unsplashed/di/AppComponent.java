package com.anythingintellect.unsplashed.di;

import com.anythingintellect.unsplashed.view.SplashImageListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    void inject(SplashImageListFragment fragment);
}
