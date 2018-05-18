package com.example.eiraj.seekdoers.ApiCalls;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eiraj.seekdoers.AppController;
import com.example.eiraj.seekdoers.Modal.Listings;
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
 * Created by Abhishek on 02-08-2017.
 */

public class GetListings implements Response.ErrorListener, Response.Listener<JSONObject> {

    ApiCallHandler apiCallHandler;
    int subCategoryId;
    int offset;
    int radius;
    double latitude;
    double longitude;
    String tag;
    String keyword;

    public List<Listings> listingsList;
    public String message;
    public int responseCode;

    String url;

    public GetListings(ApiCallHandler apiCallHandler, int subcategoryId, int offset , double latitude , String tag , double longitude , int radius , String keyword) {

        this.apiCallHandler = apiCallHandler;
        this.subCategoryId = subcategoryId;
        this.offset = offset;
        this.radius = radius;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tag = tag;
        this.keyword = keyword;

        listingsList = new ArrayList<>();

        try {
            keyword = URLEncoder.encode(keyword , "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = Contants.MAIN_URL + "/GetListingMainInfo.php" +
                "?offset=" + offset +
                "&Latitude=" + latitude +
                "&Tag=" + tag +
                "&Longitude=" + longitude +
                "&Radius=" + radius +
                "&SubCategory=" + subcategoryId +
                "&Search=" + keyword;

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

                Listings listings;
                String jsonString = payload.getJSONObject(i).toString();
                listings = gson.fromJson(jsonString, Listings.class);
                listingsList.add(listings);

            }

            apiCallHandler.onApiSuccess();


        } catch (JSONException e) {
            apiCallHandler.onApiFailure(e);
        } catch (Exception e) {
            apiCallHandler.onApiFailure(e);
        }


    }
}
