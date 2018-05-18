package com.example.eiraj.seekdoers.ApiCalls;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eiraj.seekdoers.AppController;
import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.Contants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 26-08-2017.
 */

public class PostComments implements Response.ErrorListener, Response.Listener<JSONObject> {


    public int responseCode;
    public String message;
    public List<SubCategory> subCategoryList;

    String url;

    int categoryID;

    ApiCallHandler apiCallHandler;

    public PostComments(ApiCallHandler apiCallHandler , int listingID , String userID , String userName , String userEmail , String comment , int rating ) {

        http://seekdoers.com/

        subCategoryList = new ArrayList<>();

        this.apiCallHandler = apiCallHandler;
        this.categoryID = categoryID;

        try {
            comment = URLEncoder.encode(comment , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = Contants.MAIN_URL + "/AddReview.php?" +
                "post_id=" + listingID +
                "&subscriber_id=0" +
                "&subscriber_name=" + userName +
                "&subscriber_email=" + userEmail +
                "&subscriber_comment=" + comment +
                "&subscriber_rating=" + rating  ;

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

            apiCallHandler.onApiSuccess();


        } catch (JSONException e) {
            apiCallHandler.onApiFailure(e);
        } catch (Exception e) {
            apiCallHandler.onApiFailure(e);
        }

    }

}
