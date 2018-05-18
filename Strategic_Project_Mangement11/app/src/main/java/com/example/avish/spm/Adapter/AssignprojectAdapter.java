package com.example.avish.spm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.CompoundButton;


import com.example.avish.spm.R;


public class AssignprojectAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AssignListView> singleRowArrayList;
    private LayoutInflater inflater;
    private ArrayList<AssignListView> chkBoxArrayList;
    private AssignListView s;

    public AssignprojectAdapter(Context context, ArrayList<AssignListView> singleRowArrayList) {
        this.context = context;
        this.singleRowArrayList = singleRowArrayList;
    }

    @Override
    public int getCount() {
        return singleRowArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return singleRowArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.projectrow1, viewGroup, false);


        TextView txtName = (TextView) view.findViewById(R.id.projectlist1);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox1);


        final AssignListView singleRow = singleRowArrayList.get(i);


        txtName.setText(singleRow.getName());


        checkBox.setOnCheckedChangeListener(myChecked);
        checkBox.setTag(i);
        checkBox.setChecked(singleRow.checked);

        chkBoxArrayList = new ArrayList<>();

        return view;
    }

    AssignListView getSingleRow(int pos) {
        return ((AssignListView) getItem(pos));
    }

    /*ArrayList<SingleRow> getCheckedBox() {
        ArrayList<SingleRow> chkBox = new ArrayList<>();
        for (SingleRow s : singleRowArrayList) {
            if (s.box) {
                chkBox.add(s);
            }
        }
        return chkBox;
    }*/

    public ArrayList<AssignListView> getChkBoxArrayList() {
        return chkBoxArrayList;
    }

    CompoundButton.OnCheckedChangeListener myChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            // getSingleRow((Integer) compoundButton.getTag()).box = b;
            if (b) {
                int pos = (int) compoundButton.getTag();
                s = singleRowArrayList.get(pos);
                s.setChecked(b);
                chkBoxArrayList.add(s);
            } else {
                int pos = (int) compoundButton.getTag();
                s = singleRowArrayList.get(pos);
                /*s.setChecked(b);
                chkBoxArrayList.remove(pos);*/
                if (chkBoxArrayList.contains(s)) {
                    chkBoxArrayList.remove(s);
                }
            }
        }
    };
}
