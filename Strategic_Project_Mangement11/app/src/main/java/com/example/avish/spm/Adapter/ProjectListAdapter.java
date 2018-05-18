package com.example.avish.spm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.avish.spm.R;

import java.util.ArrayList;

/**
 * Created by Avish on 08-05-2017.
 */

public class ProjectListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProjectRow> singlerow;
    private LayoutInflater inflator;

    public ProjectListAdapter(Context context, ArrayList<ProjectRow> singlerow) {
        this.context = context;
        this.singlerow = singlerow;

    }

    @Override
    public int getCount() {
        return singlerow.size();
    }

    @Override
    public Object getItem(int position) {
        return singlerow.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.projectrow, parent, false);
        TextView txt1 = (TextView) convertView.findViewById(R.id.projectlist);
        ProjectRow s = singlerow.get(position);
        String projectname = s.getTitle();
        int projectid = s.getProjectID();
        txt1.setText(projectname);
        return convertView;
    }

}
