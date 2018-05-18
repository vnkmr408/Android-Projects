package com.example.eiraj.seekdoers.Utils;

/**
 * Created by Abhishek on 31-07-2017.
 */

public interface ApiCallHandler {

    void onApiSuccess();
    void onApiFailure( Exception c );
    void onNoConnection();


}
