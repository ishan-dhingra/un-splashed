package com.anythingintellect.unsplashed.viewmodel;

import android.databinding.ObservableField;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.unsplashed.model.SplashImage;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class ItemSplashImageViewModel {

    private SplashImage splashImg;
    private final RequestManager requestManager;

    public ItemSplashImageViewModel(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public SplashImage getSplashImg() {
        return splashImg;
    }

    public void setSplashImg(SplashImage splashImg) {
        this.splashImg = splashImg;
    }
}
