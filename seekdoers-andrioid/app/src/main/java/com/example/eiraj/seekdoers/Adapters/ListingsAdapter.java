package com.example.eiraj.seekdoers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eiraj.seekdoers.Modal.Listings;
import com.example.eiraj.seekdoers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

 

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.myholder> {

    public List<Listings> listingsList;

    Context context;

    public interface OnItemClickListener
    {
        void onItemClick(Listings listings);
    }

    OnItemClickListener listener;

    public ListingsAdapter(Context context, List listingsList, OnItemClickListener listener ) {

        this.context = context;
        this.listingsList = listingsList;
        this.listener = listener;

    }


    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listings_adapter, parent, false);
        return new myholder(itemView);

    }

    @Override
    public void onBindViewHolder(myholder holder, int position) {

        final Listings listings = listingsList.get(position);

        holder.listingTitleTextView.setText(listings.getJobTitle());
        holder.address1TextView.setText(listings.getAddress());
        holder.address2TextView.setText(listings.getCity() + ", " + listings.getCountryLo() );
        holder.phoneTextView.setText( listings.getPhone() );

        if(listings.getAvatarImage().compareTo("") !=  0) {
            Picasso.with(context)
                    .load(listings.getAvatarImage())
                    .placeholder(R.drawable.listingsbackground)
                    .into(holder.listingProfileImageView);
            holder.listingProfileImageView.setVisibility(View.VISIBLE);
        }

        if(listings.getMainImage().compareTo("") !=  0) {
            Picasso.with(context)
                    .load(listings.getMainImage())
                    .into(holder.listingBackgroundImageView);
        }

        for(ImageView star:holder.starsList)
            star.setImageResource(R.drawable.star_empty);


        if(listings.getRating().compareTo("") != 0)
        {

            if(listings.getRating().compareTo("")!= 0 ) {

                float rating = Float.parseFloat(listings.getRating());

                rating = Math.round(rating*100)/100;

                holder.ratingTextView.setText( String.valueOf(rating) );

                int i = 0;
                while (i < Math.floor(rating))
                {


                    holder.starsList.get(i).setImageResource(R.drawable.star_filled);
                    i++;

                }


                rating -= i;

                if( rating-0.5 >= 0)
                    holder.starsList.get(i).setImageResource(R.drawable.star_half);

            }



        }

        holder.selfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(listings);
            }
        });

    }

    public class myholder extends RecyclerView.ViewHolder {

        public ImageView listingProfileImageView;
        public ImageView listingBackgroundImageView;
        public TextView listingTitleTextView;
        public TextView address1TextView;
        public TextView address2TextView;
        public TextView phoneTextView;
        public ArrayList<ImageView> starsList;
        public TextView ratingTextView;

        public View selfView;

        public myholder(View itemView) {

            super(itemView);
            selfView = itemView;
            listingBackgroundImageView = (ImageView) itemView.findViewById(R.id.listings_activity_listing_background);
            listingProfileImageView = (ImageView) itemView.findViewById(R.id.listings_activity_listing_image);
            listingTitleTextView = (TextView) itemView.findViewById(R.id.listings_activity_listings_title_text_view);
            address1TextView = (TextView) itemView.findViewById(R.id.listings_activity_address1_text_view);
            address2TextView = (TextView) itemView.findViewById(R.id.listings_activity_address2_text_view);
            phoneTextView = (TextView) itemView.findViewById(R.id.listings_activity_phone_text_view);
            ratingTextView = (TextView) itemView.findViewById(R.id.listing_rating);
            starsList = new ArrayList<>();
            starsList.add((ImageView) itemView.findViewById(R.id.listing_star1));
            starsList.add((ImageView) itemView.findViewById(R.id.listing_star2));
            starsList.add((ImageView) itemView.findViewById(R.id.listing_star3));
            starsList.add((ImageView) itemView.findViewById(R.id.listing_star4));
            starsList.add((ImageView) itemView.findViewById(R.id.listing_star5));

        }
    }

    @Override
    public int getItemCount()
    {
        return listingsList.size();
    }
}
