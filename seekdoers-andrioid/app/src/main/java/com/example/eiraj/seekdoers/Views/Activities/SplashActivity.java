package com.example.eiraj.seekdoers.Views.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.example.eiraj.seekdoers.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Eiraj on 4/10/2017.
 */

public class SplashActivity extends Activity
{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    public static boolean isLoggedIn;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        sharedpreferences = getSharedPreferences("", Context.MODE_PRIVATE);

        isLoggedIn = sharedpreferences.getBoolean("isLoggedIn" , false);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


                if(isLoggedIn)
                {

                    String email = sharedpreferences.getString("userEmail" , "");
                    Log.i("Seekdoers" , email);

                    Intent i = new Intent(SplashActivity.this,  HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }
                else
                {

                    Intent i = new Intent(SplashActivity.this,  HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }



                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
