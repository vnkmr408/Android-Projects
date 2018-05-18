package com.example.eiraj.seekdoers.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Abhishek Aggarwal on 3/7/2018.
 */

public class CommonFunctions {

    public static void updateProfile(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Contants.MAIN_URL + "/myaccount/edit-account/"));
        activity.startActivity(intent);
    }

}
