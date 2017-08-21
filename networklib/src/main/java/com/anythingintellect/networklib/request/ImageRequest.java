package com.anythingintellect.networklib.request;

import android.content.res.ObbInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class ImageRequest extends Request<Bitmap> {

    // Lock to acquire while parsing a image, so we are just doing one at a time
    private static final Object globalLock = new Object();

    public ImageRequest(String url, Method method, Response.Listener<Bitmap> listener) {
        super(url, method, listener);
    }

    @Override
    public Response<Bitmap> parseResponse(RawResponse rawResponse) {
        synchronized (globalLock) {
            try {
                byte[] data = rawResponse.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                return Response.success(bitmap);
            } catch (OutOfMemoryError outOfMemoryError) {
                return Response.error(new ResponseError("Not enough memory for image decoding", outOfMemoryError, rawResponse));
            }
        }
    }
}
