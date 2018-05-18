package com.example.avish.spm.Activity;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.avish.spm.Adapter.EmployeeListAdapter;
import com.example.avish.spm.R;
import com.example.avish.spm.Adapter.AssignListView;
import com.example.avish.spm.Adapter.AssignprojectAdapter;
import com.example.avish.spm.Adapter.AsssignProjectSpinner;
import com.example.avish.spm.Adapter.EmployeeRow;

import com.example.avish.spm.Adapter.SpinnerRowAssign;
import com.example.avish.spm.ApiConfiguration.ApiConfiguration;
import com.example.avish.spm.httprequestprocessor.HttpRequestProcessor;
import com.example.avish.spm.httprequestprocessor.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vinay on 15-05-2017.
 */

public class AssignProjectFragment extends Fragment {
    private AssignListView assignListView;
    private ArrayList<AssignListView> singleRowArrayList;
    private Toolbar toolbar;
    private Spinner spinner;
    private ListView listView;
    private int pID;
    String mID;
    private String name;
    private String lname;
    private int id;
    private String address;
    private String mothername;
    private String designation;
    private EditText edtstartdate, edtenddate;
    private HttpRequestProcessor httpRequestProcessor;
    private Response response;
    private ApiConfiguration apiConfiguration;
    private String baseURL, urlassignproject, urlprojectlist, jsonResponseString, urlmemberlist;
    private Button btnassignproject;
    private boolean success;
    private ListView lv;
    private EmployeeRow employeeRow;
    private SpinnerRowAssign spinnerRowAssign;
    private ArrayList<SpinnerRowAssign> projectRowArrayList;
    private AsssignProjectSpinner projectAdapter;
    private String title, description, startDate, endDate, projectType;
    private int projectID;
    private int memberid;
    String projectID1;
    String selectedMemberID;
    private AssignprojectAdapter assignprojectAdapter;
    private ArrayAdapter<SpinnerRowAssign> adapter;
    JSONArray memberArray;
    String jsonStringToPost;
    JSONObject jsonObject;
    EmployeeListAdapter employeeListAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assign__project, container, false);
        lv = (ListView) view.findViewById(R.id.lvemployee);
        // toolbar = (Toolbar) view.findViewById(R.id.toolbarassign);
        spinner = (Spinner) view.findViewById(R.id.spinner3);

        btnassignproject = (Button) view.findViewById(R.id.btnassign);
        //  toolbar.setTitle("Assign Projects");
        //Initialization
        httpRequestProcessor = new HttpRequestProcessor();
        response = new Response();
        apiConfiguration = new ApiConfiguration();

        //Getting BaseURL
        baseURL = apiConfiguration.getApi();
        urlprojectlist = baseURL + "ProjectAPI/GetProjectListing";
        urlmemberlist = baseURL + "MemberAPI/GetApplicationMemberList";
        urlassignproject = baseURL + "ProjectMemberAssociationAPI/AddNewAssociation";
        projectRowArrayList = new ArrayList<>();
        singleRowArrayList = new ArrayList<>();
        new ProjectListtask().execute();
        new MemberListTask().execute();

        projectAdapter = new AsssignProjectSpinner(getActivity(), projectRowArrayList);
        assignprojectAdapter = new AssignprojectAdapter(getActivity(), singleRowArrayList);
        // adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,singleRowArrayList);
        spinner.setAdapter(projectAdapter);
        lv.setAdapter(assignprojectAdapter);

        btnassignproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer buffer = new StringBuffer();
                SpinnerRowAssign assignlistvalue = (SpinnerRowAssign) spinner.getSelectedItem();
                pID = assignlistvalue.getId();
                projectID1 = String.valueOf(pID);
                //Toast.makeText(AssignProjects.this,pID,Toast.LENGTH_LONG).show();
                Log.d("test", String.valueOf(pID));

                //Getting checked members from the listview
                ArrayList<AssignListView> checkedMemberList = assignprojectAdapter.getChkBoxArrayList();
                for (AssignListView m : checkedMemberList) {
                    // String name = m.getName();
                    int id = m.getId();
                    mID = String.valueOf(id);
                    buffer.append(mID + "/");
                }
                Log.d("test", buffer.toString());
                selectedMemberID = buffer.toString();

                new AssignTask().execute(projectID1, selectedMemberID);
            }
        });

        return view;
    }

    /*---------------------------------------------ProjectList---VIEW--------------------------------------------------*/
    public class ProjectListtask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urlprojectlist);


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

                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);
                        projectID = object.getInt("ProjectId");
                        title = object.getString("Title");
                        description = object.getString("Description");
                        startDate = object.getString("StartDate");
                        endDate = object.getString("EndDate");
                        projectType = object.getString("ProjectType");

                        spinnerRowAssign = new SpinnerRowAssign(projectID, title);
                        projectRowArrayList.add(spinnerRowAssign);
                    }
                    projectAdapter.notifyDataSetChanged();


                } else {

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

    /*------------------------------------------MemberList----view-------------------------------------------------------*/
    public class MemberListTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            jsonResponseString = httpRequestProcessor.gETRequestProcessor(urlmemberlist);


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

                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);
                        memberid = object.getInt("MemberId");
                        name = object.getString("FName");
                        lname = object.getString("LName");
                        address = object.getString("Address");
                        mothername = object.getString("MotherName");
                        designation = object.getString("Designation");

                        assignListView = new AssignListView(name, memberid);
                        singleRowArrayList.add(assignListView);
                    }

                    assignprojectAdapter.notifyDataSetChanged();

                } else {

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }


    }

    /*-----------------------------------------------------------assign-----------------------------------*/
    private class AssignTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            //JSONObject jsonObject = new JSONObject();
            projectID1 = params[0];
            selectedMemberID = params[1];
            String[] mID = selectedMemberID.split("/");
            int length = mID.length;

            jsonObject = new JSONObject();

            memberArray = new JSONArray();
            for (int i = 0; i < mID.length; i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("ProjectMemberId", mID[i]);
                    memberArray.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                jsonObject.put("ProjectId", projectID);
                jsonObject.put("MemberId", "");
                jsonObject.put("StartDate", "");
                jsonObject.put("EndDate", "");
                jsonObject.put("Description", "");
                jsonObject.put("Status", "");
                jsonObject.put("ProjectMemberList", memberArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonStringToPost = jsonObject.toString();
            response = httpRequestProcessor.pOSTRequestProcessor(jsonStringToPost, urlassignproject);
            jsonResponseString = response.getJsonResponseString();
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
              String  message = jsonObject.getString("message");
                Log.d("mesage", message);


                if (success) {
                    JSONArray responseData = jsonObject.getJSONArray("responseData");
                    for (int i = 0; i < responseData.length(); i++) {
                        JSONObject object = (JSONObject) responseData.get(i);


                    }
                    adapter.notifyDataSetChanged();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Assign Projects");
    }


}
