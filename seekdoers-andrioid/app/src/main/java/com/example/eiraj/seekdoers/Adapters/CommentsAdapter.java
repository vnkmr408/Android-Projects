package com.example.eiraj.seekdoers.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.Comment;
import com.example.eiraj.seekdoers.R;

import java.util.ArrayList;
import java.util.List;




public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyHolder> {

    public List<Comment> commentList;

    public CommentsAdapter(List<Comment> commentList)
    {
        this.commentList = commentList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comments_adapter,parent,false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Comment comment = commentList.get(position);
        holder.authorNameTextView.setText( comment.getComment_author() );
        holder.authorCommentsTextView.setText(  comment.getComment_content() );
        if( comment.getRating().compareTo("") !=0 )
        {

            int rating = Integer.parseInt(comment.getRating());
            for(int i=0;i<rating;i++)
            {
                holder.starList.get(i).setImageResource(R.drawable.star_filled);
            }

        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public TextView authorNameTextView;
        public TextView commentsDateTextView;
        public TextView authorCommentsTextView;
        public ImageView authorImageView;
        public List<ImageView> starList;

        public MyHolder(View itemView) {

            super(itemView);
            authorCommentsTextView = (TextView) itemView.findViewById(R.id.authorCommentTextView);
            authorNameTextView = (TextView) itemView.findViewById(R.id.authorNameTextView);
            commentsDateTextView = (TextView) itemView.findViewById(R.id.commentDateTextView);
            authorImageView = (ImageView) itemView.findViewById(R.id.authorImageView);
            starList = new ArrayList<>();
            starList.add((ImageView) itemView.findViewById(R.id.commentStarImageView1));
            starList.add((ImageView) itemView.findViewById(R.id.commentStarImageView2));
            starList.add((ImageView) itemView.findViewById(R.id.commentStarImageView3));
            starList.add((ImageView) itemView.findViewById(R.id.commentStarImageView4));
            starList.add((ImageView) itemView.findViewById(R.id.commentStarImageView5));

        }
    }


}
