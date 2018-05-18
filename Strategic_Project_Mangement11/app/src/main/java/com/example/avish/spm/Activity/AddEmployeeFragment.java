package com.example.avish.spm.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avish.spm.R;
import com.example.avish.spm.ApiConfiguration.ApiConfiguration;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Avish on 15-05-2017.
 */

public class AddEmployeeFragment extends Fragment {

    private Toolbar toolbar;
    private EditText edtName;
    private EditText edtdesignation;
    private EditText edtEmailID;
    private EditText edtPhone;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtaddress;
    private String name, designation, emailID, phone, userName, password, address;
    private Button btnaddemployee;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlRegister;
    private String jsonPostString, jsonResponseString;
    private int responsedata;
    private boolean success;
    private String subject;
    private String m = "Username";
    private String n = "Password";
    private String s = "Login to your Account";


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add__employee, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar2);
        toolbar.setTitle("Employee Information");
        edtName = (EditText) view.findViewById(R.id.editText9);
        edtdesignation = (EditText) view.findViewById(R.id.editText10);
        edtEmailID = (EditText) view.findViewById(R.id.editText14);
        edtPhone = (EditText) view.findViewById(R.id.editText12);
        edtaddress = (EditText) view.findViewById(R.id.editText15);
        edtUserName = (EditText) view.findViewById(R.id.editText16);
        edtPassword = (EditText) view.findViewById(R.id.editText17);
        btnaddemployee = (Button) view.findViewById(R.id.button7);
        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //Getting BaseURL
        baseURL = apiConfiguration.getApi();
        urlRegister = baseURL + "AccountAPI/SaveApplicationUser";

        //On clicking register Button

        btnaddemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting values
                name = edtName.getText().toString();
                designation = edtdesignation.getText().toString();
                emailID = edtEmailID.getText().toString();
                if (!isValidEmail(emailID)) {
                    edtEmailID.setError("invalid email");
                    return;
                }
                phone = edtPhone.getText().toString();
                userName = edtUserName.getText().toString();
                password = edtPassword.getText().toString();
                address = edtaddress.getText().toString();
                String to = edtEmailID.getText().toString();
//                String smsmessage = edtUserName.getText().toString();
//                String sms = edtPassword.getText().toString();
//                String smsmessage1 = m + ":  " + smsmessage;
//                String sms1 = n + ":  " + sms;
//                String sent = s + "\n" + smsmessage1 + "\n" + sms1;
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
//                email.putExtra(Intent.EXTRA_SUBJECT, subject);
//                email.putExtra(Intent.EXTRA_TEXT, sent);
//                email.setType("message/rfc822");
//                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                new RegistrationTask().execute(name, designation, emailID, phone, userName, password, address);
            }
        });
        return view;
    }

    protected void sendsms() {
        String tophonenumber = "+91" + edtPhone.getText().toString();
        String smsmessage = edtUserName.getText().toString();
        String sms = edtPassword.getText().toString();
        String smsmessage1 = m + ":  " + smsmessage;
        String sms1 = n + ":  " + sms;
        String sent = s + "\n" + smsmessage1 + "\n" + sms1;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(tophonenumber, null, sent, null, null);

    }

    private boolean isValidEmail(String email) {
        String Email_PATTERN = "^[A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(Email_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private class RegistrationTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            name = params[0];
            designation = params[1];
            emailID = params[2];
            phone = params[3];
            userName = params[4];
            password = params[5];
            address = params[6];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("FName", name);
                jsonObject.put("LName", "");
                jsonObject.put("MemberCode", "");
                jsonObject.put("UserTypeId", 4);
                jsonObject.put("Address", address);
                jsonObject.put("Designation", designation);
                jsonObject.put("EmailId", emailID);
                jsonObject.put("MobileNo", phone);
                jsonObject.put("Gender", "");
                jsonObject.put("DateOfBirth", "");
                jsonObject.put("FatherName", "");
                jsonObject.put("MotherName", "");
                jsonObject.put("UserName", userName);
                jsonObject.put("Password", password);

                jsonPostString = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonPostString, urlRegister);
                jsonResponseString = response.getJsonResponseString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
               // sendsms();
                JSONObject jsonObject = new JSONObject(s);
                responsedata = jsonObject.getInt("responseData");
                Log.d("responseData", String.valueOf(responsedata));


                if (responsedata == 1) {
                    Toast.makeText(getActivity(), " Added Sucessfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "username already exists try another", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

                /*if (success) {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                }*/

        }

    }
}
