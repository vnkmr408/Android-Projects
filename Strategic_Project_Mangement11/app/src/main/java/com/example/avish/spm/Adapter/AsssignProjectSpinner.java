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
 * Created by Avish on 12-05-2017.
 */

public class AsssignProjectSpinner extends BaseAdapter {

    private Context context;
    private ArrayList<SpinnerRowAssign> singlerow;
    private LayoutInflater inflator;

    public AsssignProjectSpinner(Context context, ArrayList<SpinnerRowAssign> singlerow) {
        this.context = context;
        this.singlerow = singlerow;
        // this.inflator = inflator;
    }

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
        convertView = inflator.inflate(R.layout.spinnerrow, parent, false);
        TextView txt1 = (TextView) convertView.findViewById(R.id.txtid);
        // TextView txt2= (TextView) convertView.findViewById(R.id.txtname);
        SpinnerRowAssign s = singlerow.get(position);
        // String projectname = s.getId() + ""+s.getName();
        String name = s.getName();

        txt1.setText(name);

        return convertView;
    }
}
