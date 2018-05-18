package com.example.avish.spm.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.NetworkInfo;

import com.example.avish.spm.ApiConfiguration.ApiConfiguration;
import com.example.avish.spm.Employee_Information.EmployeeHomeScreen;
import com.example.avish.spm.R;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText edtName, edtPasswd;
    private Button btnLogin;
    private TextView textview;
    private String name, passwd;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private int useridentitykey;
    private String fname;
    private String lname;
    private String emailid;
    private int mobile;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlLogin, jsonStringToPost, jsonResponseString;
    private boolean success;
    private String errorMessage;
    String answer;
    private int userid, userTypeID;
    private int applicationuserid;
    private SharedPreferences sharedprefrences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Checking for session.Key to session is ApplicationUSerID stored in SharedPreference

        sharedprefrences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        applicationuserid = sharedprefrences.getInt("ApplicationUserId", 0);
        userTypeID = sharedprefrences.getInt("UserTypeId", 0);

        if (applicationuserid == 0) {


            //findViewById
            edtName = (EditText) findViewById(R.id.editText1);
            edtPasswd = (EditText) findViewById(R.id.editText3);
            btnLogin = (Button) findViewById(R.id.button);


            //Initialization
            httpRequestProcessor = new HttpRequestProcessor();
            response = new Response();
            apiConfiguration = new ApiConfiguration();

            //Getting base url
            baseURL = apiConfiguration.getApi();
            urlLogin = baseURL + "AccountAPI/GetLoginUser";

            //On
            //
            //
            //
            // clicking login button
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Getting name and password
                    name = edtName.getText().toString();
                    passwd = edtPasswd.getText().toString();
                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    if (null != activeNetwork) {

                    } else
                        answer = "No internet Connectivity";
                    Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();


                    new LoginTask().execute(name, passwd);

                }
            });
        } else {
            if (userTypeID == 1) {
                //Admin login
                startActivity(new Intent(Login.this, Navigation.class));
            } else if (userTypeID == 4) {
                startActivity(new Intent(Login.this, EmployeeHomeScreen.class));
            }
        }

    }


    public class LoginTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            name = params[0];
            passwd = params[1];

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserName", name);
                jsonObject.put("Password", passwd);


                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonStringToPost, urlLogin);
                jsonResponseString = response.getJsonResponseString();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("Response String", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("success", String.valueOf(success));
                errorMessage = jsonObject.getString("ErrorMessage");

                if (errorMessage.equals("User Authenticated!!")) {
                    userid = jsonObject.getInt("UserTypeId");
                    useridentitykey = jsonObject.getInt("UserIdentityKey");
                    fname = (String) jsonObject.get("FName");
                    lname = (String) jsonObject.get("LName");
                    emailid = (String) jsonObject.get("EmailId");
                    mobile = jsonObject.getInt("MobileNo");
                    applicationuserid = jsonObject.getInt("ApplicationUserId");

                    sharedprefrences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    editor = sharedprefrences.edit();
                    editor.putInt("UserTypeId", userid);
                    editor.putInt("useridentity_key", useridentitykey);
                    editor.putString("firstname_key", String.valueOf(fname));
                    editor.putString("lastname_key", String.valueOf(lname));
                    editor.putString("emailid_key", String.valueOf(emailid));
                    editor.putInt("mobno_key", (Integer) mobile);
                    editor.putInt("ApplicationUserId", applicationuserid);
                    editor.commit();


                    if (userid == 1) {
                        Log.d("ErrorMessage", errorMessage);

                        Intent intent = new Intent(Login.this, Navigation.class);

                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();

                        Toast.makeText(Login.this, "Admin", Toast.LENGTH_LONG).show();
                    } else if (userid == 4) {

                        Intent intent = new Intent(Login.this, EmployeeHomeScreen.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Login.this, "employee login ", Toast.LENGTH_SHORT).show();
                    }

                } else if (errorMessage.equals("Invalid username!!")) {
                    Toast.makeText(Login.this, "invalid username", Toast.LENGTH_SHORT).show();
                } else if (errorMessage.equals("Invalid password!!")) {
                    Toast.makeText(Login.this, "invalid password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}


