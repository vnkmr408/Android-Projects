package com.example.avish.spm.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.avish.spm.ApiConfiguration.ApiConfiguration;
import com.example.avish.spm.R;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyInformation extends Fragment {

    private View view;
    private TextView txtName, designation, address, txtmobilenumbeer, txtemailaddress, txtdobname, txtgender, txtmemberid;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL;
    private String urlRegister;
    private String jsonResponseString;
    private String getloadprofile;
    private boolean success;
    private String message;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.myinfofragment, container, false);

        txtName = (TextView) view.findViewById(R.id.txtName);
        designation = (TextView) view.findViewById(R.id.designation);
        address = (TextView) view.findViewById(R.id.address);
        txtmobilenumbeer = (TextView) view.findViewById(R.id.txtMobileNumber);
        txtemailaddress = (TextView) view.findViewById(R.id.txtEmailAddress);
        txtdobname = (TextView) view.findViewById(R.id.txtdobName);
        txtgender = (TextView) view.findViewById(R.id.txtgenderName);
        txtmemberid = (TextView) view.findViewById(R.id.txtmemberidName);


        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //Getting BaseURL
        baseURL = apiConfiguration.getApi();
        sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int mID = sharedPreferences.getInt("useridentity_key", 0);

        getloadprofile = baseURL + "MemberAPI/GetMemberProfile/" + mID;
        new LoadProfilee().execute();
        return view;
    }

    private class LoadProfilee extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            // JSONObject jsonObject = new JSONObject();


            jsonResponseString = httpRequestProcessor.gETRequestProcessor(getloadprofile);
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Response String", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("Success", String.valueOf(success));
                message = jsonObject.getString("message");
                Log.d("mesage", message);


                if (success) {
                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);

                        String name = object.getString("FName");
                        String designationn = object.getString("Designation");
                        String addres = object.getString("Address");
                        String mobno = object.getString("MobileNo");
                        String email = object.getString("EmailId");
                        String dob = object.getString("DateOfBirth");
                        String memberid = object.getString("MemberId");
                        String gender = object.getString("Gender");


                        txtName.setText("Name : Admin");
                        designation.setText("Designation : null");
                        address.setText("Address : jammu" );
                        txtmobilenumbeer.setText("Mobile Number : 7898837851" );
                        txtemailaddress.setText("Email Id : admin@gmail.com");
                        txtdobname.setText("Date of birth : 1993-09-28" );
                        txtmemberid.setText("Member Id : " + memberid);
                        txtgender.setText("Gender : " + gender);

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("My Information");
    }
}
