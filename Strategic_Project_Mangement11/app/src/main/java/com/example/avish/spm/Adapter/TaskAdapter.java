package com.example.avish.spm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.avish.spm.R;

import java.util.ArrayList;


public class TaskAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TaskList> taskArrayList;
    private LayoutInflater inflater;

    public TaskAdapter(Context context, ArrayList<TaskList> taskArrayList) {
        this.context = context;
        this.taskArrayList = taskArrayList;
    }

    @Override
    public int getCount() {
        return taskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.task_activity, parent, false);

        TextView txt1 = (TextView) convertView.findViewById(R.id.txtid);
        TextView txt2 = (TextView) convertView.findViewById(R.id.txtname);
        TextView txt3 = (TextView) convertView.findViewById(R.id.txtdescription);


        TaskList taskList = taskArrayList.get(position);


        String pname = taskList.getProjectname();
        String pdescription = taskList.getProjectdesscription();


        txt2.setText(pname);
        txt3.setText(pdescription);
        return convertView;


    }
}
