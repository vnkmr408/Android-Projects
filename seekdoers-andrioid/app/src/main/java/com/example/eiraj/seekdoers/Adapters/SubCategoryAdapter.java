package com.example.eiraj.seekdoers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.myholder> {

    private List<SubCategory> subCategoryList;

    private SubCategoryAdapter.OnItemClickListener onItemClickListener;

    private Context context;

    public SubCategoryAdapter(Context context ,List subCategoryList, SubCategoryAdapter.OnItemClickListener listener){

        this.subCategoryList = subCategoryList;
        this.onItemClickListener = listener;
        this.context = context;

    }


    @Override
    public SubCategoryAdapter.myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subcategory_adapter,parent,false);
        return new SubCategoryAdapter.myholder(itemView);
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.myholder holder, int position) {

        final SubCategory subCategory =subCategoryList.get(position);
        holder.subCategoryTextView.setText(subCategory.getJob_category());

        Picasso.with(context)
                .load(subCategory.getImage())
                .into(holder.subCategoryImageView);

        holder.selfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(subCategory);
            }
        });

    }

    public class myholder extends RecyclerView.ViewHolder{

        public TextView subCategoryTextView;
        public ImageView subCategoryImageView;
        public View selfView;

        public myholder(View itemView) {

            super(itemView);
            selfView = itemView;
            subCategoryTextView= (TextView) itemView.findViewById(R.id.subcategory_text_view);
            subCategoryImageView = (ImageView) itemView.findViewById(R.id.subCategoryImageView);

        }
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(SubCategory subCategory);
    }

}
