package com.example.eiraj.seekdoers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.Tag;
import com.example.eiraj.seekdoers.R;

import java.util.List;

 

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.myholder>{

    List<Tag> tagList;

    public TagsAdapter(List<Tag> tagList)
    {
        this.tagList = tagList;
    }


    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_adapter,parent,false);
        return new myholder(itemView);
    }

    @Override
    public void onBindViewHolder(myholder holder, final int position) {

        holder.tagsTextView.setText( tagList.get(position).getJob_category() );
        holder.tagsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tagList.get(position).setSelected(isChecked);
            }
        });
        holder.tagsSwitch.setSelected(tagList.get(position).isSelected());
        holder.tagsSwitch.setChecked(tagList.get(position).isSelected());

    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class myholder extends RecyclerView.ViewHolder{

        public TextView tagsTextView;
        public Switch tagsSwitch;

        public myholder(View itemView) {

            super(itemView);
            tagsTextView = (TextView) itemView.findViewById(R.id.tagsTextView);
            tagsSwitch = (Switch) itemView.findViewById(R.id.tagsSwitch);

        }

    }


}
