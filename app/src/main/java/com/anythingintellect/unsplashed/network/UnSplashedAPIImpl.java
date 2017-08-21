package com.anythingintellect.unsplashed.network;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.networklib.request.JSONArrayRequest;
import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;
import com.anythingintellect.unsplashed.model.SplashImage;
import com.anythingintellect.unsplashed.util.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class UnSplashedAPIImpl implements UnSplashedAPI{

    private final RequestManager requestManager;

    public UnSplashedAPIImpl(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void getImageList(boolean skipCache, final Response.Listener<List<SplashImage>> listListener) {
        JSONArrayRequest jsonArrayRequest = new JSONArrayRequest(Constant.IMAGE_LIST_URL, Request.Method.GET, new Response.Listener<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                List<SplashImage> listResponse = parseJSON(response);
                listListener.onSuccess(listResponse);
            }

            @Override
            public void onError(ResponseError error) {
                listListener.onError(error);
            }
        });
        jsonArrayRequest.setSkipCache(skipCache);
        requestManager.addRequest(jsonArrayRequest);
    }

    // Any 3rd party library can be used to make this process more efficent
    // given that our requirement is simple, I am handling the parsing myself
    private List<SplashImage> parseJSON(JSONArray response) {
        List<SplashImage> list = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject object = response.optJSONObject(i);
            // Cloud be null if server mess up, for now just keep it clean and avoiding null checks
            list.add(new SplashImage(object.optJSONObject("urls").optString("small")));
        }
        return list;
    }
}
