package com.example.eiraj.seekdoers.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.R;
import com.squareup.picasso.Picasso;

import java.util.List;
 

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.myholder> {

    Context context;

    private List<Category> categoryList;

    private OnItemClickListener onItemClickListener;

    public HomeAdapter(Context context , List categoryList, OnItemClickListener listener){

        this.context = context;
        this.categoryList = categoryList;
        this.onItemClickListener = listener;

    }


    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_adapter,parent,false);
        return new myholder(itemView);
    }

    @Override
    public void onBindViewHolder(myholder holder, int position) {

        final Category category = categoryList.get(position);
        holder.categoryTextView.setText(category.getJob_category());

        Picasso.with(context)
                .load(category.getImage())
                .centerInside()
                .fit()
                .placeholder(R.drawable.listingsbackground)
                .into(holder.categoryImageView);

        holder.selfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(category);
            }
        });

    }

    public class myholder extends RecyclerView.ViewHolder{

        public ImageView categoryImageView;
        public TextView categoryTextView;
        public View selfView;

        public myholder(View itemView) {

            super(itemView);
            selfView = itemView;
            categoryImageView= (ImageView) itemView.findViewById(R.id.category_image_view);
            categoryTextView= (TextView) itemView.findViewById(R.id.category_text_view);

        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Category plan);
    }

}
