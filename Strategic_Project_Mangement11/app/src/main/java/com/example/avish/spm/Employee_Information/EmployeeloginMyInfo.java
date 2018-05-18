package com.example.avish.spm.Employee_Information;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class EmployeeloginMyInfo extends Fragment {
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
        view = inflater.inflate(R.layout.employeelogin_myinfo, container, false);


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
        new LoadProfile().execute();
        return view;
    }

    private class LoadProfile extends AsyncTask<String, String, String> {
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


                        txtName.setText("Name : " + name);
                        designation.setText("Designation : " + designationn);
                        address.setText("Address : " + addres);
                        txtmobilenumbeer.setText("Mobile Number : " + mobno);
                        txtemailaddress.setText("Email Id : " + email);
                        txtdobname.setText("Date of birth : " + dob);
                        txtmemberid.setText("Member Id : " + memberid);
                        txtgender.setText("Gender : " + gender);

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}