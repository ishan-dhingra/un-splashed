package com.anythingintellect.networklib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.networklib.request.ImageRequest;
import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class NetworkImageView extends AppCompatImageView implements Response.Listener<Bitmap> {

    private String imgUrl = null;
    private ImageRequest imageRequest = null;
    private RequestManager requestManager = null;
    private Drawable loadingResource;

    public NetworkImageView(Context context) {
        super(context);
    }

    public NetworkImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(String url, RequestManager requestManager) {
        this.requestManager = requestManager;
        if (url == null || url.length() == 0 || requestManager == null) {
            cancel();
            return;
        }
        if (url.equalsIgnoreCase(imgUrl)) {
            return;
        }
        cancel();
        newRequest(url, requestManager);
    }

    private void newRequest(String url, RequestManager requestManager) {
        showLoading();
        imgUrl = url;
        imageRequest = new ImageRequest(imgUrl, Request.Method.GET, this);
        requestManager.addRequest(imageRequest);
    }

    private void showLoading() {
        setScaleType(ScaleType.CENTER);
        setImageDrawable(loadingResource);
    }

    private void showBitmap(Bitmap bitmap) {
        setScaleType(ScaleType.FIT_CENTER);
        setImageBitmap(bitmap);
    }

    private void cancel() {
        if (imageRequest != null) {
            imageRequest.cancel();
            imageRequest = null;
            imgUrl = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (imageRequest != null) {
            imageRequest.cancel();
            imgUrl = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        loadImage(imgUrl, requestManager);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public void onSuccess(Bitmap response) {
        showBitmap(response);
    }

    @Override
    public void onError(ResponseError error) {
        error.printStackTrace();
    }

    public Drawable getLoadingResource() {
        return loadingResource;
    }

    public void setLoadingResource(Drawable loadingResource) {
        this.loadingResource = loadingResource;
        if (imageRequest == null) {
            setImageDrawable(loadingResource);
        }
    }
}
