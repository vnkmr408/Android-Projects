package com.example.eiraj.seekdoers.Views.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eiraj.seekdoers.Adapters.CommentsAdapter;
import com.example.eiraj.seekdoers.Adapters.HomeAdapter;
import com.example.eiraj.seekdoers.Adapters.ViewPagerAdapter;
import com.example.eiraj.seekdoers.ApiCalls.GetComments;
import com.example.eiraj.seekdoers.ApiCalls.GetListingDetail;
import com.example.eiraj.seekdoers.ApiCalls.PostComments;
import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.Modal.Comment;
import com.example.eiraj.seekdoers.Modal.ListingDetail;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.CommonFunctions;
import com.example.eiraj.seekdoers.Utils.NonScrollListView;
import com.example.eiraj.seekdoers.Views.Fragments.LoadingFragment;
import com.example.eiraj.seekdoers.Views.Fragments.NoConnectionFragment;
import com.example.eiraj.seekdoers.Views.Fragments.WentWrongFragment;
import com.example.eiraj.seekdoers.Views.ReviewDialog;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListingDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiCallHandler,
        LoadingFragment.OnFragmentInteractionListener,
        WentWrongFragment.OnFragmentInteractionListener,
        NoConnectionFragment.OnFragmentInteractionListener {


    //================================================================================
    // Variables
    //================================================================================

    int listingID;
    String listingName;
    String apiName;
    String subCategoryName;

    GetListingDetail getListingDetail;
    PostComments postComments;
    GetComments getComments;

    ListingDetail listingDetail;

    List<Comment> commentList;

    Button nextImageButton;
    Button prevImageButton;
    ViewPager viewPager;

    RecyclerView commentsRecyclerView;
    LinearLayout commentsLoadingLayout;

    CommentsAdapter commentsAdapter;

    Button websiteButton;
    Button mailButton;
    Button mapButton;
    Button writeReviewButton;

    List<ImageView> starsList;

    TextView listingNameTextView;
    TextView phoneNoTextView;
    TextView addressTextView;
    TextView descriptionTextView;
    TextView subCategoryTextView;

    TextView showAllReviewsTextView;

    LoadingFragment loadingFragment;
    NoConnectionFragment noConnectionFragment;
    WentWrongFragment wentWrongFragment;

    ProgressDialog progressDialog;

    ViewPagerAdapter myCustomPagerAdapter;
    SharedPreferences sharedPreferences;
    public static boolean isLoggedIn;


    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listing_detail);

        initNav();

        initVariables();

        initGUI();

        initGUIBehaviour();

        initRecycler();

        showData();

        setTitle(listingName);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.comapany_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.profile) {
            CommonFunctions.updateProfile(this);
//            Intent intent = new Intent(getApplicationContext(),UpdateProfileActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_changepass) {
            Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            SharedPreferences sharedpreferences = getSharedPreferences("", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isLoggedIn" , false );
            editor.commit();

            LoginManager.getInstance().logOut();

            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else if(id==R.id.nav_sign_in){

            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);

        }else if(id==R.id.nav_add_listing){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://seekdoers.com/add-your-listing/"));
            startActivity(intent);



        }else if(id==R.id.nav_term_of_use){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://seekdoers.com/tos/"));
            startActivity(intent);

        }else if(id==R.id.nav_about_us){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://seekdoers.com/contact-us/"));
            startActivity(intent);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //================================================================================
    // Initializing Methods
    //================================================================================

    private void initNav()
    {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);

        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn" , false);
        if(isLoggedIn){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.profile).setVisible(true);
            nav_Menu.findItem(R.id.nav_changepass).setVisible(true);
            nav_Menu.findItem(R.id.nav_manage).setVisible(true);
            nav_Menu.findItem(R.id.nav_sign_in).setVisible(false);
        }

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initGUI()
    {

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        prevImageButton = (Button)findViewById(R.id.prebutton);
        nextImageButton = (Button)findViewById(R.id.postbutton);

        websiteButton = (Button) findViewById(R.id.websiteButton);
        mailButton = (Button) findViewById(R.id.emailButton);
        mapButton = (Button) findViewById(R.id.mapButton);
        writeReviewButton = (Button) findViewById(R.id.createReviewButton);

        listingNameTextView = (TextView) findViewById(R.id.listingNametextView);
        phoneNoTextView = (TextView) findViewById(R.id.phoneNotextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        descriptionTextView = (TextView) findViewById(R.id.listingDescriptiontextView);
        subCategoryTextView=(TextView)findViewById(R.id.subCategoriesTextView) ;

        showAllReviewsTextView = (TextView) findViewById(R.id.showAllReviewsTextView);

        starsList = new ArrayList<>();
        starsList.add( (ImageView) findViewById(R.id.starImageView1) );
        starsList.add( (ImageView) findViewById(R.id.starImageView2) );
        starsList.add( (ImageView) findViewById(R.id.starImageView3) );
        starsList.add( (ImageView) findViewById(R.id.starImageView4) );
        starsList.add( (ImageView) findViewById(R.id.starImageView5) );

        progressDialog = new ProgressDialog(ListingDetailActivity.this);

        loadingFragment = new LoadingFragment();
        noConnectionFragment = new NoConnectionFragment();
        wentWrongFragment = new WentWrongFragment();

    }

    private void initVariables()
    {

        apiName = "";
        listingID = getIntent().getIntExtra("listingID" , 0 );
        listingName = getIntent().getStringExtra("listingName");
        subCategoryName=getIntent().getStringExtra("subCategoryName");
        commentList = new ArrayList<>();

    }

    private  void initGUIBehaviour()
    {

        showAllReviewsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListingDetailActivity.this , CommentsActivity.class);
                intent.putParcelableArrayListExtra("comments" , (ArrayList<? extends Parcelable>) getComments.commentList);
                intent.putExtra("listingName" , listingName);
                intent.putExtra("rating" , listingDetail.getAvgRating() );
                startActivity(intent);

            }
        });

        prevImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
            }
        });

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1, true);
            }
        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(listingDetail.getWebsite().toString())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(listingDetail.getWebsite()));
                    startActivity(intent);
                }else{
                    Toast.makeText(ListingDetailActivity.this, "No website found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, listingDetail.getEmail() );
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(%s)", listingDetail.getLatitude() , listingDetail.getLongitude() , listingDetail.getLatitude() , listingDetail.getLongitude() , listingName  );
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        progressDialog.setCancelable(false);
        progressDialog.setMessage("Posting you Review...");

        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isLoggedIn = getSharedPreferences("", Context.MODE_PRIVATE).getBoolean("isLoggedIn" , false);
                if(isLoggedIn)
                {

                    final ReviewDialog reviewDialog = new ReviewDialog(ListingDetailActivity.this , listingName , listingID);
                    reviewDialog.setCancelable(false);
                    reviewDialog.setCanceledOnTouchOutside(false);
                    reviewDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {


                            if(reviewDialog.action.compareTo("submit") == 0) {

                                SharedPreferences sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);
                                String userName = sharedPreferences.getString("userName" , "");
                                String userEmail = sharedPreferences.getString("userEmail" , "");
                                String comment = reviewDialog.comments;
                                int rating = reviewDialog.rating;
                                apiName = "postComments";
                                postComments = new PostComments(ListingDetailActivity.this , listingID , "" , userName , userEmail , comment , rating);
                                progressDialog.show();

                            }
                        }
                    });
                    reviewDialog.show();

                }
                else
                {

                    AlertDialog.Builder builder;

                    builder = new AlertDialog.Builder((new ContextThemeWrapper( ListingDetailActivity.this  , R.style.myDialog)));

                    builder.setTitle("Seekdoers")
                            .setMessage("You are not logged in")
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("Login" , new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent( getBaseContext() , SignInActivity.class );
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton("OK" , null)
                            .show();




                }


            }
        });

    }

    private void initRecycler()
    {

        commentsLoadingLayout = (LinearLayout) findViewById(R.id.commentsLoadingLayout);
        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentsRecyclerView);

        commentsAdapter = new CommentsAdapter(commentList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        commentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        commentsRecyclerView.setAdapter(commentsAdapter);

    }

    private void showData ()
    {

        Log.i("Seekdoers" , "Showing Loader");
        getSupportFragmentManager().beginTransaction().add(R.id.listing_detail_frame_layout,loadingFragment).commit();
        SharedPreferences sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail" , "");
        getListingDetail = new GetListingDetail(this , listingID , userEmail);

    }


    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {

        if(apiName.compareTo("postComments") == 0)
        {

            progressDialog.dismiss();

            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder((new ContextThemeWrapper( ListingDetailActivity.this  , R.style.myDialog)));

            builder.setTitle("Seekdoers")
                    .setMessage("Your comment was posted successfully")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("OK" , null)
                    .show();


        }
        else if(apiName.compareTo("getComments") == 0)
        {

            commentsLoadingLayout.setVisibility(View.GONE);

            if(getComments.commentList.size() == 0)
            {
                findViewById(R.id.noCommentsTextView).setVisibility(View.VISIBLE);
            }
            else {

                for (int i=0;i<3&&i<getComments.commentList.size();i++)
                    commentList.add(getComments.commentList.get(i));

                if(getComments.commentList.size() > 3)
                    showAllReviewsTextView.setVisibility(View.VISIBLE);

                commentsAdapter.notifyDataSetChanged();
            }


        }
        else
        {

            getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();

            listingDetail = getListingDetail.listingDetail;

            listingNameTextView.setText( listingName );
            subCategoryTextView.setText(subCategoryName);
            addressTextView.setText(listingDetail.getAddress());
            phoneNoTextView.setText(listingDetail.getPhoneNo());
            descriptionTextView.setText(listingDetail.getDescription());

            if(listingDetail.getAvgRating().compareTo("")!= 0 ) {

                float rating = Float.parseFloat(listingDetail.getAvgRating());
                int i = 0;
                while (i < Math.floor(rating))
                {

                    starsList.get(i).setImageResource(R.drawable.star_filled);
                    i++;

                }


                rating -= i;

                if( rating-0.5 >= 0)
                    starsList.get(i).setImageResource(R.drawable.star_half);

            }

            if(listingDetail.getImageList().size() > 0) {
                myCustomPagerAdapter = new ViewPagerAdapter(ListingDetailActivity.this, listingDetail.getImageList());
                viewPager.setAdapter(myCustomPagerAdapter);
                findViewById(R.id.noImagesLayout).setVisibility(View.GONE);
            }

            getComments = new GetComments(this , listingID , 0);
            apiName="getComments";
        }

    }

    @Override
    public void onApiFailure(Exception c) {

        if(apiName.compareTo("postComments") == 0)
        {

            c.printStackTrace();

            progressDialog.dismiss();

            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder((new ContextThemeWrapper( ListingDetailActivity.this  , R.style.myDialog)));

            builder.setTitle("Seekdoers")
                    .setMessage("Sorry, Your comments could not be posted")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("OK" , null)
                    .show();


        }
        else
        {
            c.printStackTrace();
            getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.listing_detail_frame_layout, wentWrongFragment).commit();
        }
    }

    @Override
    public void onNoConnection() {

        if(apiName.compareTo("postComments") == 0)
        {

            progressDialog.dismiss();

            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder((new ContextThemeWrapper( ListingDetailActivity.this  , R.style.myDialog)));

            builder.setTitle("Seekdoers")
                    .setMessage("Sorry, No internet Connection")
                    .setIcon(R.drawable.logo)
                    .setPositiveButton("OK" , null)
                    .show();

        }
        else {
            getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.listing_detail_frame_layout, noConnectionFragment).commit();
        }
    }



    //================================================================================
    // Fragment Handler Methods
    //================================================================================

    @Override
    public void onNoConnectionTryAgainButtonClicked() {

        getSupportFragmentManager().beginTransaction().remove(noConnectionFragment).commit();
        showData();

    }

    @Override
    public void onNoConnectionFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoadingFragmentInteraction(Uri uri) {


    }

    @Override
    public void onWentWrongTryAgainButtonClicked() {


        getSupportFragmentManager().beginTransaction().remove(wentWrongFragment).commit();
        showData();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
