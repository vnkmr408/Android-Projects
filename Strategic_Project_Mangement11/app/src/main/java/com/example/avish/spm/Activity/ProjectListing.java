package com.example.avish.spm.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.avish.spm.Adapter.ProjectListAdapter;
import com.example.avish.spm.Adapter.ProjectRow;
import com.example.avish.spm.ApiConfiguration.ApiConfiguration;
import com.example.avish.spm.R;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Avish on 13-04-2017.
 */

public class ProjectListing extends Fragment {
    private Button button;
    private ListView listView;
    private String title, description, startDate, endDate, projectType;
    private int projectID;

    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urladdproject, urldeleteproject, jsonResponseString;
    private boolean success;
    private ListView lv;
    private ProjectRow projectRow;
    private ArrayList<ProjectRow> projectRowArrayList;
    private ProjectListAdapter projectAdapter;
    private android.content.Context Context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_listing, container, false);

        listView = (ListView) view.findViewById(R.id.list1);

        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        //Getting base url
        baseURL = apiConfiguration.getApi();
        urladdproject = baseURL + "ProjectAPI/GetProjectListing";

        projectRowArrayList = new ArrayList<>();
        new ProjectListtask().execute();

        projectAdapter = new ProjectListAdapter(getActivity(), projectRowArrayList);
        listView.setAdapter(projectAdapter);
        //listView.setOnItemClickListener( getActivity());
        registerForContextMenu(listView);
        return view;
    }


    /* ---------------------------------------------------------------------------------------------------------*/
    public class ProjectListtask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urladdproject);


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
                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);
                        projectID = object.getInt("ProjectId");
                        title = object.getString("Title");
                        description = object.getString("Description");
                        startDate = object.getString("StartDate");
                        endDate = object.getString("EndDate");
                        projectType = object.getString("ProjectType");

                        projectRow = new ProjectRow(title, description, startDate, endDate, projectType, projectID);
                        projectRowArrayList.add(projectRow);
                    }
                    projectAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(getActivity(), "not added", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }


    }

    public class DeleteProject extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urldeleteproject);
            return jsonResponseString;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonobject = new JSONObject(s);
                success = jsonobject.getBoolean("success");
                String message = jsonobject.getString("message");
                int responsedata = jsonobject.getInt("responseData");
                if (responsedata == 0) {
                    Toast.makeText(getActivity(), "Record Deleted Succesfully", Toast.LENGTH_SHORT).show();
                } else if (responsedata == 2) {
                    Toast.makeText(getActivity(), "Assigned projects cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
                ;
                projectAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        projectRow = projectRowArrayList.get(index);

        switch (item.getItemId()) {
            case R.id.delete:
               /* int index = info.position;
                projectRow = projectRowArrayList.get(index);
                int pId = projectRow.getProjectID();
*/
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete ?" + title + "?");

                // Setting Icon to Dialog


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Write your code here to invoke YES event
                        projectID = projectRow.getProjectID();
                        Log.d("pid", String.valueOf(projectID));
                        urldeleteproject = baseURL + "ProjectAPI/DeleteProject/" + projectID;
                        new DeleteProject().execute();
                        projectAdapter.notifyDataSetChanged();
                        Log.d(String.valueOf(projectID), "projectid");

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

        }
        return super.onContextItemSelected(item);

    }

}
