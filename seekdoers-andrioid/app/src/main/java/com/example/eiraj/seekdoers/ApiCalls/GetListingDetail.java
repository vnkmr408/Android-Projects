package com.example.eiraj.seekdoers.ApiCalls;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eiraj.seekdoers.AppController;
import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.Modal.ListingDetail;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.Contants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 11-08-2017.
 */

public class GetListingDetail implements Response.ErrorListener, Response.Listener<JSONObject> {

    public int responseCode;
    public String message;
    public ListingDetail listingDetail;

    String url;

    int listingID;
    String userID;

    ApiCallHandler apiCallHandler;

    public GetListingDetail(ApiCallHandler apiCallHandler , int listingID , String userID )
    {

        this.listingID = listingID;
        this.userID = userID;

        this.apiCallHandler = apiCallHandler;

        url = Contants.MAIN_URL + "/GetListingDetail.php" +
                "?post_id=" + listingID +
                "&subscriber_id=" + userID ;

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
            String jsonString = payload.getJSONObject(0).toString();
            ListingDetail listingDetail = gson.fromJson( jsonString , ListingDetail.class);

            List<String> imageLinks = new ArrayList<>();

            int start;
            int end = 0;

            while ( end < listingDetail.getGallImages().lastIndexOf("\"") ) {

                start = listingDetail.getGallImages().indexOf("\"", end);
                end = listingDetail.getGallImages().indexOf("\"", start + 1);

                String imageLink = listingDetail.getGallImages().substring(start+1,end);

                end++;

                Log.i("Seekdoers" , imageLink );

                imageLinks.add(imageLink);

            }

            listingDetail.setImageList(imageLinks);

            this.listingDetail = listingDetail;

            apiCallHandler.onApiSuccess();


        } catch (JSONException e) {
            apiCallHandler.onApiFailure(e);
        } catch (Exception e) {
            apiCallHandler.onApiFailure(e);
        }

    }



}
