package com.example.avish.spm.httprequestprocessor;

/**
 * Created by Avish on 25-04-2017.
 */


public class Response {

    String jsonResponseString;
    int responseCode;

    public String getJsonResponseString() {
        return jsonResponseString;
    }

    public void setJsonResponseString(String jsonResponseString) {
        this.jsonResponseString = jsonResponseString;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}

