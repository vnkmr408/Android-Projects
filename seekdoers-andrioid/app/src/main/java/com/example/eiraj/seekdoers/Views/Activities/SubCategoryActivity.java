package com.example.eiraj.seekdoers.Views.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eiraj.seekdoers.Adapters.SubCategoryAdapter;
import com.example.eiraj.seekdoers.ApiCalls.GetSubCategory;
import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.Modal.Tag;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;

import java.util.ArrayList;
import java.util.List;

import com.example.eiraj.seekdoers.Utils.CommonFunctions;
import com.example.eiraj.seekdoers.Views.Fragments.LoadingFragment;
import com.example.eiraj.seekdoers.Views.Fragments.NoConnectionFragment;
import com.example.eiraj.seekdoers.Views.Fragments.NoDataFragment;
import com.example.eiraj.seekdoers.Views.Fragments.WentWrongFragment;
import com.facebook.login.LoginManager;

public class SubCategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiCallHandler ,
        NoConnectionFragment.OnFragmentInteractionListener,
        WentWrongFragment.OnFragmentInteractionListener,
        LoadingFragment.OnFragmentInteractionListener,
        NoDataFragment.OnFragmentInteractionListener{

    //================================================================================
    // Variables
    //================================================================================

    GetSubCategory getSubCategory;

    RecyclerView recyclerView;

    List<SubCategory> subCategoryList;

    SubCategoryAdapter subCategoryAdapter;

    LoadingFragment loadingFragment;
    NoConnectionFragment noConnectionFragment;
    WentWrongFragment wentWrongFragment;
    NoDataFragment noDataFragment;

    String categoryName;
    int categoryID;
    SharedPreferences sharedPreferences;
    public static boolean isLoggedIn;



    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        initVariables();

        initNav();

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
        getMenuInflater().inflate(R.menu.listing_category, menu);
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


        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);

        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn" , false);
        if(isLoggedIn){
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.profile).setVisible(true);
            nav_Menu.findItem(R.id.nav_changepass).setVisible(true);
            nav_Menu.findItem(R.id.nav_manage).setVisible(true);
            nav_Menu.findItem(R.id.nav_sign_in).setVisible(false);
        }

    }

    private void initVariables()
    {
        subCategoryList = new ArrayList<>();
        categoryName = getIntent().getExtras().getString("categoryName");
        categoryID = getIntent().getExtras().getInt("categoryID");
        setTitle(categoryName);
    }

    private void initGUI()
    {

        recyclerView = (RecyclerView) findViewById(R.id.subcategory_activity_recycler_view);
        loadingFragment = new LoadingFragment();

    }

    private void initRecyclerView()
    {

        subCategoryAdapter = new SubCategoryAdapter(this , subCategoryList, new SubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SubCategory subCategory) {

                Intent intent = new Intent(getBaseContext() , ListingsActivity.class);
                intent.putExtra("subCategoryID" ,  subCategory.getTerm_id() );
                intent.putExtra("subCategoryName" ,  subCategory.getJob_category() );
                intent.putExtra("callerActivity" , "SubCategoryActivity");
                intent.putExtra("latitude" , 43.757711);
                intent.putExtra("longitude" , -79.2285656);
                intent.putExtra("tagList" , new ArrayList<Tag>());
                intent.putExtra("radius" , 50);
                intent.putExtra("keyword" , "");
                intent.putExtra("cityName" , "Mississauga");
                startActivity(intent);

            }

        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subCategoryAdapter);

    }

    private void showData ()
    {

        getSupportFragmentManager().beginTransaction().add(R.id.subcategory_activity_frame_layout,loadingFragment).commit();
        getSubCategory = new GetSubCategory( this , categoryID);

    }


    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {

        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();

        if(getSubCategory.subCategoryList.size() == 0)
        {
            noDataFragment = new NoDataFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.subcategory_activity_frame_layout, noDataFragment ).commit();
        }
        else {
            subCategoryList.addAll(getSubCategory.subCategoryList);
            subCategoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onApiFailure(Exception c) {

        c.printStackTrace();
        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
        wentWrongFragment = new WentWrongFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.subcategory_activity_frame_layout,wentWrongFragment).commit();

    }

    @Override
    public void onNoConnection() {

        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
        noConnectionFragment = new NoConnectionFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.subcategory_activity_frame_layout,noConnectionFragment).commit();

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

    @Override
    public void onNoDataFragmentInteraction(Uri uri) {

    }
}
