package com.example.eiraj.seekdoers.ApiCalls;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eiraj.seekdoers.AppController;
import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abhishek on 01-08-2017.
 */

public class PostLogin implements Response.ErrorListener, Response.Listener<JSONObject> {

    ApiCallHandler apiCallHandler;

    String userName;
    String password;
    String url;

    public String email;
    public String userDisplayName;

    public int responseCode;
    public String message;

    public PostLogin(ApiCallHandler apiCallHandler , String userName , String password)
    {

        this.apiCallHandler = apiCallHandler;
        this.userName = userName;
        this.password = password;

        url = "https://seekdoers.com/wp-json/jwt-auth/v1/token?username=" + userName +"&password=" + password ;

        Log.i("Seekdoers", "Calling Url - " + url);

        boolean isInternetConnected = AppController.isNetworkConnected();

        if (isInternetConnected) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, this, this);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, "");

        } else {

            apiCallHandler.onNoConnection();

        }

    }


    @Override
    public void onErrorResponse(VolleyError error) {

        message = "Incorrect Username or Password";
        responseCode = 100;

        if(error.getClass() == TimeoutError.class)
            message = "Request Timed Out";

        apiCallHandler.onApiFailure(error);

    }

    @Override
    public void onResponse(JSONObject response) {


        try {

            Log.i("Seekdoers", "Response - " + response.toString());

            boolean bool = response.isNull("token");

            if( !bool )
            {

                email = response.getString("user_email");
                userDisplayName = response.getString("user_display_name");
                apiCallHandler.onApiSuccess();

            }

            else
            {

//                String code = response.getString("code");
//                code = code.substring( code.indexOf("]")+2 );
//                code = code.replace("_" , " ");
//                code = AppController.toTitleCase(code);
//
//                message = code;
//                responseCode = 100;
//
//                apiCallHandler.onApiFailure(new Exception());


            }




        } catch (Exception e) {
            apiCallHandler.onApiFailure(e);
        }


    }
}
