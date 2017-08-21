package com.anythingintellect.unsplashed.network;

import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.unsplashed.model.SplashImage;

import java.util.List;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public interface UnSplashedAPI {

    void getImageList(boolean skipCache, Response.Listener<List<SplashImage>> imageList);

}
