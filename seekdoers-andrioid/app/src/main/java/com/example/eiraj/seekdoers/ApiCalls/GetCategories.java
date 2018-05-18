package com.example.eiraj.seekdoers.ApiCalls;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eiraj.seekdoers.AppController;
import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.Contants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 31-07-2017.
 */

public class GetCategories implements Response.ErrorListener, Response.Listener<JSONObject> {

    public int responseCode;
    public String message;
    public List<Category> categoryList;

    String url;

    ApiCallHandler apiCallHandler;

    public GetCategories(ApiCallHandler apiCallHandler) {

        categoryList = new ArrayList<>();

        this.apiCallHandler = apiCallHandler;

        url = Contants.MAIN_URL + "/GetCategories.php";

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

                Category category;
                String jsonString = payload.getJSONObject(i).toString();
                category = gson.fromJson(jsonString, Category.class);

                String jobTitle = category.getJob_category();
                jobTitle = jobTitle.replace("&gt; " , "");
                jobTitle = jobTitle.replace("amp;" , "");
                category.setJob_category(jobTitle);

                String slug = category.getSlug();
                slug = slug.replaceAll("-", " & ");
                slug = AppController.toTitleCase(slug);
                category.setSlug(slug);

                categoryList.add(category);

            }

            apiCallHandler.onApiSuccess();


        } catch (JSONException e) {
            apiCallHandler.onApiFailure(e);
        } catch (Exception e) {
            apiCallHandler.onApiFailure(e);
        }

    }

}
