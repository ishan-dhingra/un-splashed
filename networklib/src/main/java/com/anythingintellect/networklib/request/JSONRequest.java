package com.anythingintellect.networklib.request;

import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

@SuppressWarnings("deprecation")
public abstract class JSONRequest<T> extends Request<T> {

    private JSONObject body;
    private static final String CHAR_SET = "utf-8";

    public JSONRequest(String url, Method method, Response.Listener<T> listener) {
        super(url, method, listener);
    }



    public void setJsonBody(JSONObject jsonBody) {
        this.body = jsonBody;
    }

    @Override
    public byte[] getBody() {
        if (body == null) {
            return null;
        }
        try {
            return body.toString().getBytes(CHAR_SET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getContentType() {
        return "application/json; charset="+CHAR_SET;
    }
}
