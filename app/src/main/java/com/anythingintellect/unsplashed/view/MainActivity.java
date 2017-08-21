package com.anythingintellect.unsplashed.view;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.networklib.cache.Cache;
import com.anythingintellect.networklib.cache.InMemoryCache;
import com.anythingintellect.networklib.network.DefaultHttpDriver;
import com.anythingintellect.networklib.network.DefaultNetworkDriver;
import com.anythingintellect.networklib.network.HttpDriver;
import com.anythingintellect.networklib.network.NetworkDriver;
import com.anythingintellect.networklib.request.JSONArrayRequest;
import com.anythingintellect.networklib.request.JSONRequest;
import com.anythingintellect.networklib.request.Request;
import com.anythingintellect.networklib.response.DefaultResponseHandler;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;
import com.anythingintellect.networklib.response.ResponseHandler;
import com.anythingintellect.networklib.view.NetworkImageView;
import com.anythingintellect.unsplashed.R;

import org.json.JSONArray;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity{

    RequestManager requestManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            SplashImageListFragment fragment = new SplashImageListFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
// Take it out
        Cache cache = new InMemoryCache();
        // 4 mb
        cache.init(1024 * 1024 * 4);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpDriver httpDriver = new DefaultHttpDriver(builder.build());
        NetworkDriver networkDriver = new DefaultNetworkDriver(httpDriver);
        ResponseHandler responseHandler = new DefaultResponseHandler(new Handler());
        requestManager = new RequestManager(cache, networkDriver, 4, responseHandler);
        requestManager.start();
//        findViewById(R.id.button).setOnClickListener(this);
    }
//
//    private void doTestRequestOne() {
//        JSONRequest request = new JSONArrayRequest("http://pastebin.com/raw/wgkJgazE", Request.Method.GET, this);
//        requestManager.addRequest(request);
//        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView);
//        imageView.loadImage("https://images.unsplash.com/photo-1464550838636-1a3496df938b", requestManager);
//    }
//
//    private boolean toggle = true;
//    private void doTestRequestTwo() {
//        JSONRequest request = new JSONArrayRequest("http://pastebin.com/raw/wgkJgazE", Request.Method.GET, this);
//        requestManager.addRequest(request);
//        NetworkImageView imageView = (NetworkImageView) findViewById(R.id.imageView);
//        imageView.loadImage("https://images.unsplash.com/photo-1464537356976-89e35dfa63ee", requestManager);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        requestManager.stop();
//    }
//
//    @Override
//    public void onSuccess(JSONArray response) {
//        TextView txt = (TextView) findViewById(R.id.text);
//        txt.setText(response.toString());
//    }
//
//    @Override
//    public void onError(ResponseError error) {
//        TextView txt = (TextView) findViewById(R.id.text);
//        txt.setText("Error " + error.getMessage());
//        error.printStackTrace();
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (toggle) {
//            doTestRequestOne();
//        } else {
//            doTestRequestTwo();
//        }
//        toggle = !toggle;
//    }
}

