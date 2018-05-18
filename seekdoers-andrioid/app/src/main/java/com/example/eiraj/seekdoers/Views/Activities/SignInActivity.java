package com.example.eiraj.seekdoers.Views.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.eiraj.seekdoers.ApiCalls.PostLogin;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.Contants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity implements ApiCallHandler, GoogleApiClient.OnConnectionFailedListener {

    LoginButton facebookLoginButton;

    CallbackManager callbackManager;

    GoogleApiClient mGoogleApiClient;

    PostLogin postLogin;

    ProgressDialog progressDialog;

    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initGUI();

        initFacebook();

        initGmail();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if(result.isSuccess()){

                    GoogleSignInAccount account= result.getSignInAccount();
                    String name= account.getDisplayName();
                    String email= account.getEmail();

                    SharedPreferences sharedpreferences = getSharedPreferences("", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("isLoggedIn" , true );
                    editor.putString("userEmail" , email );
                    editor.putString("userName" , name);
                    editor.commit();

                    Intent intent = new Intent(getBaseContext() , HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


            }
        }

        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}



    //================================================================================
    // Initializing Methods
    //================================================================================

    private void initGUI()
    {

        progressDialog = new ProgressDialog(this );
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final EditText usernameEditText = (EditText) findViewById(R.id.editText);
        final EditText passwordEditText = (EditText) findViewById(R.id.editText2);
        Button signin = (Button) findViewById(R.id.signin);

        final ApiCallHandler apiCallHandler = this;

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( !usernameEditText.getText().toString().equals("")) {

                    if ( !passwordEditText.getText().toString().equals("")) {

                        progressDialog.setTitle("Seekdoers");
                        progressDialog.setIcon(R.drawable.logo);
                        progressDialog.setMessage("Logging In");
                        progressDialog.show();

                        String userName = usernameEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        postLogin = new PostLogin( apiCallHandler , userName , password);

                    }

                    else
                    {
                        showMessage("Please enter a valid password");
                    }
                }

                else
                {
                    showMessage("Please enter a valid username");
                }

            }
        });

    }

    private void initFacebook()
    {

        facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginButton.setReadPermissions(Arrays.asList("email" , " public_profile"));

        callbackManager = CallbackManager.Factory.create();

        FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());


                                try {

                                    SharedPreferences sharedpreferences = getSharedPreferences("", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean("isLoggedIn" , true );
                                    editor.putString("userEmail" , object.getString("email") );
                                    editor.putString("userName" , object.getString("name"));
                                    editor.commit();

                                    Intent intent = new Intent(getBaseContext() , HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager , facebookCallback );

        findViewById(R.id.facebookImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLoginButton.performClick();
            }
        });

    }

    private void initGmail()
    {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        findViewById(R.id.googleImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 101);
            }
        });

    }

    private void showMessage (String msg)
    {

        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Seekdoers")
                .setMessage(msg)
                .setIcon(R.drawable.logo)
                .setPositiveButton("OK" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();

    }



    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {


        progressDialog.dismiss();

        SharedPreferences sharedpreferences = getSharedPreferences("", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isLoggedIn" , true );
        editor.putString("userEmail" , postLogin.email );
        editor.putString("userName" , postLogin.userDisplayName);

        editor.commit();

        Intent intent = new Intent(getBaseContext() , HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onApiFailure(Exception c) {

        progressDialog.dismiss();

        if(postLogin.responseCode == 100)
            showMessage(postLogin.message);


    }

    @Override
    public void onNoConnection() {

    }


    public void signUp(View view){

        startActivity(new Intent(this, SignUpActivity.class));

    }


    public void onForgetPasswordClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Contants.MAIN_URL + "/myaccount/lost-password/"));
        startActivity(intent);
    }
}
