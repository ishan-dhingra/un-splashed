package com.anythingintellect.unsplashed.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;
import com.anythingintellect.unsplashed.model.SplashImage;
import com.anythingintellect.unsplashed.network.UnSplashedAPI;
import com.anythingintellect.unsplashed.view.SplashImageListFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class SplashImageListViewModel {

    private UnSplashedAPI unSplashedAPI;
    private final ObservableList<SplashImage> splashImages;
    private final ObservableField<Boolean> showProgress;
    private final ObservableField<Boolean> showError;

    @Inject
    public SplashImageListViewModel(UnSplashedAPI unSplashedAPI) {
        this.unSplashedAPI = unSplashedAPI;
        this.splashImages = new ObservableArrayList<>();
        this.showError = new ObservableField<>();
        this.showProgress = new ObservableField<>(true);
    }

    public void loadSplashImageList(boolean skipCache) {
        showError.set(false);
        showProgress.set(true);
        unSplashedAPI.getImageList(skipCache, new Response.Listener<List<SplashImage>>() {
            @Override
            public void onSuccess(List<SplashImage> response) {
                showProgress.set(false);
                splashImages.clear();
                splashImages.addAll(response);
            }

            @Override
            public void onError(ResponseError error) {
                showProgress.set(false);
                showError.set(true);

            }
        });
    }

    public ObservableField<Boolean> getShowError() {
        return showError;
    }

    public ObservableField<Boolean> getShowProgress() {
        return showProgress;
    }

    public ObservableList<SplashImage> getSplashImages() {
        return splashImages;
    }
}
