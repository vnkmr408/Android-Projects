package com.example.eiraj.seekdoers.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.Tag;
import com.example.eiraj.seekdoers.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Tag> {

    private Context mContext;
    private ArrayList<Tag> tagArrayList;
    private SpinnerAdapter myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapter(Context context, int resource, List<Tag> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.tagArrayList = (ArrayList<Tag>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.tagTextView);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.tagCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(tagArrayList.get(position).getJob_category());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(tagArrayList.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int getPosition = (Integer) buttonView.getTag();
                Tag tag = tagArrayList.get(getPosition);
                tag.setSelected(!tag.isSelected());

            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }


}
