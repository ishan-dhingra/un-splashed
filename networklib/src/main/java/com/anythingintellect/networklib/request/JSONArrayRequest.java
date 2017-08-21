package com.anythingintellect.networklib.request;

import com.anythingintellect.networklib.response.RawResponse;
import com.anythingintellect.networklib.response.Response;
import com.anythingintellect.networklib.response.ResponseError;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class JSONArrayRequest extends JSONRequest<JSONArray> {

    public JSONArrayRequest(String url, Method method, Response.Listener<JSONArray> listener) {
        super(url, method, listener);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Response<JSONArray> parseResponse(RawResponse rawResponse) {
        // For now only considering 200 as valid response, other can be added later.
        if (rawResponse.getStatusCode() == HttpStatus.SC_OK) {
            String jsonString = new String(rawResponse.getData());
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                return Response.success(jsonArray);
            } catch (JSONException e) {
                return Response.error(new ResponseError("Error parsing JSON", e, rawResponse));
            }

        } else {
            return Response.error(new ResponseError("Invalid HTTP Status " + rawResponse.getStatusCode(),
                    null, rawResponse));
        }
    }
}
