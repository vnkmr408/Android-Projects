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
import android.widget.ListView;


import com.example.avish.spm.R;
import com.example.avish.spm.Adapter.TaskAdapter;
import com.example.avish.spm.Adapter.TaskList;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;
import com.example.avish.spm.ApiConfiguration.ApiConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class EmployeeloginMyTask extends Fragment {
    private ListView lv;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlRegister;
    private String jsonPostString, jsonResponseString;
    private String message;
    private boolean success;
    private TaskList taskList;
    private ArrayList<TaskList> taskListArrayList;
    private TaskAdapter taskAdapter;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.employeelogin_mytasks, container, false);


        ListView lv3 = (ListView) v.findViewById(R.id.Mytasklist);
        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();


        baseURL = apiConfiguration.getApi();
        sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int mID = sharedPreferences.getInt("useridentity_key", 0);

        urlRegister = baseURL + "ProjectMemberAssociationAPI/GetMyProjectList/" + mID;
        taskListArrayList = new ArrayList<>();
        taskAdapter = new TaskAdapter(getActivity(), taskListArrayList);
        lv3.setAdapter(taskAdapter);

        new MyTask().execute();
        return v;
    }


    private class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {


            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urlRegister);

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
                if (success) {
                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);

                        String name = object.getString("ProjectName");
                        Log.d("ProjectName", name);

                        String description = object.getString("Description");
                        Log.d("Description", description);

                        taskList = new TaskList(name, description);
                        taskListArrayList.add(taskList);
                    }
                    taskAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
