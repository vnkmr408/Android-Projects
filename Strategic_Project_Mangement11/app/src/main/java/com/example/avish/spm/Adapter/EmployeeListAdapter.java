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

public class EmployeeListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<EmployeeRow> singlerow;
    private LayoutInflater inflator;

    public EmployeeListAdapter(Context context, ArrayList<EmployeeRow> singlerow) {
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
        convertView = inflator.inflate(R.layout.employeerow, parent, false);
        TextView txt1 = (TextView) convertView.findViewById(R.id.employeelist);
        TextView txt2 = (TextView) convertView.findViewById(R.id.emplist);
        EmployeeRow s = singlerow.get(position);
        String projectname = s.getFname() + "   " + s.getLname();
        String designation = s.getDesignation();
        int memberid = s.getMemberid();
        txt1.setText(projectname);
        txt2.setText(designation);
        return convertView;
    }

}
