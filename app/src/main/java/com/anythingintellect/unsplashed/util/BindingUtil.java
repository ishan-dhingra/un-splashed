package com.anythingintellect.unsplashed.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.networklib.view.NetworkImageView;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class BindingUtil {

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("bind:showLoading")
    public static void showLoading(SwipeRefreshLayout swipeRefreshLayout, boolean visible) {
        swipeRefreshLayout.setRefreshing(visible);
    }

    @BindingAdapter({"bind:imgUrl", "bind:requestManager"})
    public static void loadImage(NetworkImageView networkImageView, String url, RequestManager requestManager) {
        networkImageView.loadImage(url, requestManager);
    }

    @BindingAdapter("bind:loadingResource")
    public static void setLoadingResource(NetworkImageView networkImageView, Drawable resource) {
        networkImageView.setLoadingResource(resource);
    }
}
