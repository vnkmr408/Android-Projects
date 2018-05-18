package com.example.eiraj.seekdoers.Views.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Adapters.CommentsAdapter;
import com.example.eiraj.seekdoers.Modal.Comment;
import com.example.eiraj.seekdoers.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    List<Comment> commentList;
    String listingName;
    String rating;

    List<ImageView> starsList;

    RecyclerView commentsRecyclerView;

    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentList = getIntent().getParcelableArrayListExtra("comments");
        listingName = getIntent().getStringExtra("listingName");
        rating = getIntent().getStringExtra("rating");

        ((TextView) findViewById(R.id.comment_listing_title)).setText(listingName);
        ((TextView) findViewById(R.id.comments_size_text_view)).setText(commentList.size() + " ratings");

        starsList = new ArrayList<>();
        starsList.add( (ImageView) findViewById(R.id.comment_activity_star1) );
        starsList.add( (ImageView) findViewById(R.id.comment_activity_star2) );
        starsList.add( (ImageView) findViewById(R.id.comment_activity_star3) );
        starsList.add( (ImageView) findViewById(R.id.comment_activity_star4) );
        starsList.add( (ImageView) findViewById(R.id.comment_activity_star5) );

        if(rating.compareTo("")!= 0 ) {

            float ratingF = Float.parseFloat(rating);
            int i = 0;
            while (i < Math.floor(ratingF))
            {

                starsList.get(i).setImageResource(R.drawable.star_filled);
                i++;

            }

            ratingF -= i;

            if( ratingF-0.5 >= 0)
                starsList.get(i).setImageResource(R.drawable.star_half);

        }

        commentsAdapter = new CommentsAdapter(commentList);

        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentsRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        commentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        commentsRecyclerView.setAdapter(commentsAdapter);
        commentsRecyclerView.setNestedScrollingEnabled(false);

    }
}
