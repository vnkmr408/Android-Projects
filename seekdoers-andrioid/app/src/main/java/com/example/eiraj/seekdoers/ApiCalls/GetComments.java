package com.example.eiraj.seekdoers.ApiCalls;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eiraj.seekdoers.AppController;
import com.example.eiraj.seekdoers.Modal.Comment;
import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.Contants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 30-08-2017.
 */

public class GetComments implements Response.ErrorListener, Response.Listener<JSONObject> {

    public int responseCode;
    public String message;
    public List<Comment> commentList;

    String url;

    int listingID;
    int offset;

    ApiCallHandler apiCallHandler;

    public GetComments(ApiCallHandler apiCallHandler , int listingID , int offset) {

        commentList = new ArrayList<>();

        this.apiCallHandler = apiCallHandler;
        this.listingID=listingID;
        this.offset = offset;

        url = Contants.MAIN_URL + "/GetCommentList.php?post_id=" + listingID +
                "&offset=" + offset  ;

        Log.i("Seekdoers", "Calling Url - " + url);

        boolean isInternetConnected = AppController.isNetworkConnected();

        if (isInternetConnected) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, "");

        } else {

            apiCallHandler.onNoConnection();

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        apiCallHandler.onApiFailure(error);

    }

    @Override
    public void onResponse(JSONObject response) {

        try {

            Log.i("Seekdoers", "Response - " + response.toString());

            message = response.getString("message");
            responseCode = response.getInt("responseCode");

            JSONArray payload = response.getJSONArray("Payload");

            Gson gson = new Gson();

            for (int i = 0; i < payload.length(); i++) {

                Comment comment;
                String jsonString = payload.getJSONObject(i).toString();
                Log.i("Seekdoers", "Comment - " + jsonString);
                comment = gson.fromJson(jsonString, Comment.class);

                commentList.add(comment);

            }

            apiCallHandler.onApiSuccess();


        } catch (JSONException e) {
            apiCallHandler.onApiFailure(e);
        } catch (Exception e) {
            apiCallHandler.onApiFailure(e);
        }

    }

}
