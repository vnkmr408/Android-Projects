package com.example.avish.spm.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.avish.spm.ApiConfiguration.ApiConfiguration;
import com.example.avish.spm.R;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Avish on 15-05-2017.
 */

public class AddProjectFragment extends Fragment {

    private EditText edtName, edtdescription, edtduration;
    private Button btnaddproject;
    private String name, description, duration;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urladdproject, jsonStringToPost, jsonResponseString;
    private boolean success;
    private String errorMessage;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.activity_add_project, container, false);
        edtName = (EditText) view.findViewById(R.id.editText);
        edtdescription = (EditText) view.findViewById(R.id.editText2);

        btnaddproject = (Button) view.findViewById(R.id.button3);
        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        //Getting base url
        baseURL = apiConfiguration.getApi();
        urladdproject = baseURL + "ProjectAPI/AddNewProject";
        btnaddproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting name and password
                name = edtName.getText().toString();
                description = edtdescription.getText().toString();
                // duration = edtduration.getText().toString();
                new AddProjectTask().execute(name, description);

            }
        });
        return view;
    }

    public class AddProjectTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            name = params[0];
            description = params[1];
            // duration=params[2];
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Title", name);
                jsonObject.put("Description", description);

                jsonStringToPost = jsonObject.toString();
                response = httpRequestProcessor.pOSTRequestProcessor(jsonStringToPost, urladdproject);
                jsonResponseString = response.getJsonResponseString();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonResponseString;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("Response String", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                success = jsonObject.getBoolean("success");
                Log.d("success", String.valueOf(success));

                if (success == true) {
                    Toast.makeText(getActivity(), " added successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), Navigation.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "not added", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
    }
}
