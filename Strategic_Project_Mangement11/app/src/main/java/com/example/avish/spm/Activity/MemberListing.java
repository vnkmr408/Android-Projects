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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.avish.spm.Adapter.EmployeeListAdapter;
import com.example.avish.spm.Adapter.EmployeeRow;
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

public class MemberListing extends Fragment {
    private Button button;
    private ListView listView;
    private int memberid;
    private String fname;
    private String lname;
    private String address;
    private String mothername;
    private String designation;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urladdproject, jsonStringToPost, jsonResponseString;
    private boolean success;
    private ListView lv;
    private EmployeeRow employeeRow;
    private ArrayList<EmployeeRow> projectRowArrayList;
    private EmployeeListAdapter employeeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.member_list, container, false);

        listView = (ListView) view.findViewById(R.id.list2);

        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();
        //Getting base url
        baseURL = apiConfiguration.getApi();
        urladdproject = baseURL + "MemberAPI/GetApplicationMemberList";
        projectRowArrayList = new ArrayList<>();

        new ProjectListtask().execute();
        employeeAdapter = new EmployeeListAdapter(getActivity(), projectRowArrayList);
        listView.setAdapter(employeeAdapter);
        registerForContextMenu(listView);
        return view;
    }

    public class DeleteMember extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urladdproject);
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
                    Toast.makeText(getActivity(), "Assigned member cannot be Deleted", Toast.LENGTH_SHORT).show();
                }
                ;
                employeeAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

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
                        memberid = object.getInt("MemberId");
                        fname = object.getString("FName");
                        lname = object.getString("LName");
                        address = object.getString("Address");
                        mothername = object.getString("MotherName");
                        designation = object.getString("Designation");

                        employeeRow = new EmployeeRow(memberid, fname, lname, address, mothername, designation);
                        projectRowArrayList.add(employeeRow);
                    }
                    employeeAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(getActivity(), "not added", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Icon to Dialog

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        memberid = employeeRow.getMemberid();
                        urladdproject = baseURL + "ProjectMemberAssociationAPI/DeleteProjectAssociation" + memberid;
                        new DeleteMember().execute();
                        employeeAdapter.notifyDataSetChanged();
                        // Write your code here to invoke YES event

                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getActivity(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

        }
        return super.onContextItemSelected(item);

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

}