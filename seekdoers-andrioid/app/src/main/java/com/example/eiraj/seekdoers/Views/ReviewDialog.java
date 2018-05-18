package com.example.eiraj.seekdoers.Views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eiraj.seekdoers.R;

import java.util.ArrayList;

/**
 * Created by Abhishek on 12-08-2017.
 */

public class ReviewDialog extends Dialog {

    public String action;
    public int rating;
    public String comments;


    ArrayList<ImageView> starsList;
    int listingID;
    String listingName;

    ProgressBar progressBar;

    public ReviewDialog(@NonNull Context context , String listingName , int listingID) {

        super(context);

        this.listingID = listingID;
        this.listingName = listingName;

        initDialog();

    }


    private void initDialog()
    {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.review_dialog);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams wmlp = this.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(wmlp);

        starsList = new ArrayList<>();

        starsList.add((ImageView) findViewById(R.id.review_star1));
        starsList.add((ImageView) findViewById(R.id.review_star2));
        starsList.add((ImageView) findViewById(R.id.review_star3));
        starsList.add((ImageView) findViewById(R.id.review_star4));
        starsList.add((ImageView) findViewById(R.id.review_star5));

        rating = 0;

        for (ImageView starImageView: starsList)
        {

            starImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int tag = Integer.parseInt((String) v.getTag());
                    rating = tag;
                    for(int i =0;i<5;i++)
                    {

                        if(i<tag)
                        {
                            starsList.get(i).setImageResource(R.drawable.star_filled);
                        }
                        else
                        {
                            starsList.get(i).setImageResource(R.drawable.star_empty);
                        }

                    }

                }
            });

        }

        ((TextView)findViewById(R.id.review_listing_title)).setText(listingName);

        findViewById(R.id.cancel_review_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                action = "cancel";
                dismiss();

            }
        });

        findViewById(R.id.submit_review_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comments = String.valueOf(((EditText)findViewById(R.id.commentsEditText)).getText());
                action = "submit";
                dismiss();

            }
        });


    }


}
