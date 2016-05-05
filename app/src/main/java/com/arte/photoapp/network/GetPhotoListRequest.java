package com.arte.photoapp.network;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.arte.photoapp.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetPhotoListRequest {

    private static String PHOTO_LIST_URL = "http://jsonplaceholder.typicode.com/photos";

    public interface Callbacks {
        void onGetPhotoListSuccess(List<Photo> photoList);

        void onGetPhotoListError();
    }

    private Context mContext;
    private Callbacks mCallbacks;

    public GetPhotoListRequest(Context context, Callbacks callbacks) {
        mContext = context;
        mCallbacks = callbacks;
    }

    public void execute() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, PHOTO_LIST_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Photo> photoList = new ArrayList<Photo>();
                Log.i(this.getClass().getSimpleName(), response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Photo photo = new Photo();
                        JSONObject currentObject = response.getJSONObject(i);
                        photo.setId("" + currentObject.getInt("id"));
                        photo.setTitle(currentObject.getString("title"));
                        photo.setUrl(currentObject.getString("url"));
                        photo.setThumbnailUrl(currentObject.getString("thumbnailUrl"));
                        photoList.add(photo);
                    } catch (JSONException e) {
                        Log.e(this.getClass().getSimpleName(), "Error deserializando JSON", e);
                        mCallbacks.onGetPhotoListError();
                        return;
                    }
                }
                // TODO transform JSONArray to List<Photo>
                mCallbacks.onGetPhotoListSuccess(photoList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(this.getClass().getSimpleName(), error.toString());
                mCallbacks.onGetPhotoListError();
            }
        });

        RequestQueueManager.getInstance(mContext).addToRequestQueue(request);
    }
}
