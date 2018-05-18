package com.example.eiraj.seekdoers.Views.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eiraj.seekdoers.ApiCalls.GetListings;
import com.example.eiraj.seekdoers.Modal.Listings;
import com.example.eiraj.seekdoers.Modal.Tag;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Adapters.ListingsAdapter;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.CommonFunctions;
import com.example.eiraj.seekdoers.Utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import com.example.eiraj.seekdoers.Views.Fragments.LoadingFragment;
import com.example.eiraj.seekdoers.Views.Fragments.NoConnectionFragment;
import com.example.eiraj.seekdoers.Views.Fragments.NoDataFragment;
import com.example.eiraj.seekdoers.Views.Fragments.WentWrongFragment;
import com.facebook.login.LoginManager;

public class ListingsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiCallHandler,
        LoadingFragment.OnFragmentInteractionListener,
        WentWrongFragment.OnFragmentInteractionListener,
        NoConnectionFragment.OnFragmentInteractionListener,
        NoDataFragment.OnFragmentInteractionListener{


    //================================================================================
    // Variables
    //================================================================================

    int subCategoryID;
    int offset;
    int radius;
    double latitude;
    double longitude;
    String tagString;
    String cityName;
    String keyword;

    String callerActivity;

    GetListings getListings;

    List<Listings> listingsList;
    ArrayList<Tag> tagList;
    String subCategoryTitle;
    ApiCallHandler apiCallHandler;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ListingsAdapter listingsAdapter;

    LoadingFragment loadingFragment;
    NoConnectionFragment noConnectionFragment;
    WentWrongFragment wentWrongFragment;
    NoDataFragment noDataFragment;

    Fragment currentFragment;

    Boolean isReloaded;
    Boolean isThereMore;

    private final static int FILTER_REQUEST_CODE = 101;

    SharedPreferences sharedPreferences;
    public static boolean isLoggedIn;


    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        initNav();

        initVariables();

        initGUI();

        initRecyclerView();

        showData();

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
        getMenuInflater().inflate(R.menu.listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.filterButton) {

            //new FilterDialog(ListingsActivity.this).show();
            recyclerView.setVisibility(View.INVISIBLE);

            if(currentFragment != null)
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.listings_activity_frame_layout,loadingFragment).commit();
            currentFragment = loadingFragment;

            Intent intent = new Intent( this , FilterActivity.class );
            if(callerActivity.compareTo("HomeActivity") == 0)
                callerActivity +="+ListingActivity";
            intent.putExtra("callerActivity" , callerActivity);
            intent.putExtra("latitude" , latitude);
            intent.putExtra("longitude" , longitude);
            intent.putExtra("tagList" , tagList);
            intent.putExtra("radius" , radius);
            intent.putExtra("cityName" , cityName);
            intent.putExtra("keyword" , keyword);
            intent.putExtra("subCategoryID" , subCategoryID);
            intent.putExtra("subCategoryName" , subCategoryTitle);
            startActivityForResult( intent , FILTER_REQUEST_CODE );

        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( requestCode == FILTER_REQUEST_CODE )
        {

            if(resultCode == RESULT_OK)
            {

                latitude = data.getDoubleExtra("latitude" , 0 );
                longitude = data.getDoubleExtra("longitude" , 0);
                radius = data.getIntExtra("radius" , 50);
                cityName = data.getStringExtra("cityName");
                tagList = data.getParcelableArrayListExtra("tags");
                subCategoryID = data.getIntExtra("subCategoryID" , 0);
                subCategoryTitle = data.getStringExtra("subCategoryName");


                keyword = data.getStringExtra("keyword");
                tagString = "";
                for(Tag tag: tagList)
                {
                    if(tag.isSelected()) {
                        if (tagString.compareTo("") == 0) {
                            tagString+=tag.getTerm_id();
                        }
                        else
                        {
                            tagString+=("_"+tag.getTerm_id());
                        }
                    }
                }
                listingsList.clear();
                offset = 0;
                getListings = new GetListings( this , subCategoryID , offset , latitude , tagString, longitude , radius , keyword);

            }

            if(resultCode == RESULT_CANCELED)
            {

                offset = 0;
                getListings = new GetListings( this , subCategoryID , offset , latitude, tagString, longitude , radius , keyword );

            }

        }

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

    private void initVariables()
    {

        listingsList = new ArrayList<>();
        subCategoryTitle = getIntent().getExtras().getString("subCategoryName");
        subCategoryID = getIntent().getExtras().getInt("subCategoryID");
        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        tagList = getIntent().getExtras().getParcelableArrayList("tagList");
        radius = getIntent().getExtras().getInt("radius");
        cityName = getIntent().getExtras().getString("cityName");
        callerActivity = getIntent().getExtras().getString("callerActivity");
        keyword = getIntent().getExtras().getString("keyword");

        tagString = "";
        for(Tag tag: tagList)
        {
            if(tag.isSelected()) {
                if (tagString.compareTo("") == 0) {
                    tagString+=tag.getTerm_id();
                }
                else
                {
                    tagString+=("_"+tag.getTerm_id());
                }
            }
        }

        apiCallHandler = this;
        isReloaded = false;
        isThereMore = true;
        offset = 0;
        setTitle(subCategoryTitle);

    }

    private void initGUI()
    {

        recyclerView = (RecyclerView) findViewById(R.id.listings_activity_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.listings_progress_bar);
        loadingFragment = new LoadingFragment();
        noConnectionFragment = new NoConnectionFragment();
        wentWrongFragment = new WentWrongFragment();

    }

    private void initRecyclerView()
    {

        listingsAdapter= new ListingsAdapter( getBaseContext() ,listingsList, new ListingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Listings listings) {

//                Toast.makeText(getApplicationContext(), ""+subCategoryID+""+subCategoryTitle, Toast.LENGTH_SHORT).show();
//
//                Log.i("CATEGORY",""+subCategoryID+""+subCategoryTitle);

                Intent intent = new Intent( ListingsActivity.this , ListingDetailActivity.class );
                intent.putExtra("listingID" , listings.getID() );
                intent.putExtra("listingName" , listings.getJobTitle() );
                intent.putExtra("subCategoryName",subCategoryTitle);
                startActivity(intent);

            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(listingsAdapter);

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore() {

                if(isThereMore) {

                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getListings = new GetListings(apiCallHandler, subCategoryID, offset, latitude, tagString,longitude, radius , keyword);
                        }
                    }, 1500);
                }
            }
        });

    }

    private void showData ()
    {

        getSupportFragmentManager().beginTransaction().add(R.id.listings_activity_frame_layout,loadingFragment).commit();
        currentFragment = loadingFragment;
        getListings = new GetListings( this , subCategoryID , offset , latitude , tagString,longitude , radius , keyword );

    }



    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {

        if(offset == 0) {

            getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
            currentFragment = null;

        }
        else
            progressBar.setVisibility(View.GONE);


        if(isReloaded){

            getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
            currentFragment = null;

        }

        offset += getListings.listingsList.size();

        if(getListings.listingsList.size() == 0 )
        {

            if(offset == 0)
            {
                noDataFragment = new NoDataFragment();
                currentFragment = noDataFragment;
                getSupportFragmentManager().beginTransaction().add(R.id.listings_activity_frame_layout, noDataFragment ).commit();
            }
            else
            {
                isThereMore=false;
            }

        }

        else {
            listingsList.addAll(getListings.listingsList);
            listingsAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onApiFailure(Exception c) {

        c.printStackTrace();
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.listings_activity_frame_layout , wentWrongFragment).commit();

    }

    @Override
    public void onNoConnection() {

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.listings_activity_frame_layout , noConnectionFragment).commit();

    }



    //================================================================================
    // Fragment Handler Methods
    //================================================================================

    @Override
    public void onNoConnectionTryAgainButtonClicked() {

        isReloaded = true;
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

        isReloaded = true;
        getSupportFragmentManager().beginTransaction().remove(wentWrongFragment).commit();
        showData();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNoDataFragmentInteraction(Uri uri) {
    }

}
