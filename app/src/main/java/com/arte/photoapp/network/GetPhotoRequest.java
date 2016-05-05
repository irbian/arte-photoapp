package com.arte.photoapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arte.photoapp.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPhotoRequest {

    private static String PHOTO_BASE_URL = "http://jsonplaceholder.typicode.com/photos/";

    public interface Callbacks {
        void onGetPhotoSuccess(Photo photo);

        void onGetPhotoError();
    }

    private Context mContext;
    private Callbacks mCallbacks;
    private String mId;

    public GetPhotoRequest(Context context, Callbacks callbacks, String id) {
        mContext = context;
        mCallbacks = callbacks;
        mId = id;
    }

    public void execute() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getGetPhotoUrl(mId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Photo photo = new Photo();
                Log.i(this.getClass().getSimpleName(), response.toString());
                try {
                    photo.setId("" + response.getInt("id"));
                    photo.setTitle(response.getString("title"));
                    photo.setUrl(response.getString("url"));
                    photo.setThumbnailUrl(response.getString("thumbnailUrl"));
                } catch (JSONException e) {
                    Log.e(this.getClass().getSimpleName(), "Error deserializando JSON", e);
                    mCallbacks.onGetPhotoError();
                    return;
                }
                mCallbacks.onGetPhotoSuccess(photo);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(this.getClass().getSimpleName(), error.toString());
                mCallbacks.onGetPhotoError();
            }
        });
        RequestQueueManager.getInstance(mContext).addToRequestQueue(request);
    }

    private String getGetPhotoUrl(String id) {
        return PHOTO_BASE_URL + id;
    }
}
