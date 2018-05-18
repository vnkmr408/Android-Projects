package com.example.eiraj.seekdoers.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eiraj.seekdoers.R;
import com.squareup.picasso.Picasso;

import java.util.List;
 

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int images[];

    List<String> imageLinks;

    LayoutInflater layoutInflater;


    public ViewPagerAdapter(Context context, List<String> imageLink) {

        this.context = context;
        this.imageLinks = imageLink;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return imageLinks.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Picasso.with(context)
                .load(imageLinks.get(position))
                //.centerCrop()
                .centerInside()

                .fit()
                .placeholder(R.drawable.logo)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
