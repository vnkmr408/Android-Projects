package com.example.eiraj.seekdoers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.R;

import java.util.ArrayList;
import java.util.List;

 

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.myholder> {

    public List<SubCategory> subCategoryList;
    List<SubCategory> originalSubCategoryList;

    private OnItemClickListener onItemClickListener;

    public SelectCategoryAdapter(List<SubCategory> subCategoryList , OnItemClickListener onItemClickListener)
    {

        this.onItemClickListener = onItemClickListener;
        originalSubCategoryList = subCategoryList;
        this.subCategoryList = new ArrayList<>();
        this.subCategoryList.addAll(originalSubCategoryList);

    }

    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_select_category,parent,false);
        return new SelectCategoryAdapter.myholder(itemView);
    }

    @Override
    public void onBindViewHolder(myholder holder, int position) {

        final SubCategory subCategory = subCategoryList.get(position);
        holder.categoryTextView.setText(subCategory.getJob_category());

        holder.selfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(subCategory);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public class myholder extends RecyclerView.ViewHolder{

        public TextView categoryTextView;
        public View selfView;

        public myholder(View itemView) {

            super(itemView);
            selfView = itemView;
            categoryTextView= (TextView) itemView.findViewById(R.id.selectCategoryTextView);

        }
    }

    public interface OnItemClickListener{
        void onItemClick(SubCategory subCategory);
    }

    public void filter ( String query )
    {

        query = query.toLowerCase();

        subCategoryList.clear();

        if(query.isEmpty()){
            subCategoryList.addAll(originalSubCategoryList);
        }
        else
        {

            for ( SubCategory subCategory : originalSubCategoryList) {

                if (subCategory.getJob_category().toLowerCase().contains(query)) {

                    subCategoryList.add(subCategory);

                }

            }
        }

        notifyDataSetChanged();

    }

}
