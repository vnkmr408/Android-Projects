package com.example.eiraj.seekdoers.Views.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.eiraj.seekdoers.ApiCalls.GetCategories;
import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.Modal.Tag;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Adapters.HomeAdapter;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;

import java.util.ArrayList;
import java.util.List;

import com.example.eiraj.seekdoers.Utils.CommonFunctions;
import com.example.eiraj.seekdoers.Utils.Contants;
import com.example.eiraj.seekdoers.Views.Fragments.LoadingFragment;
import com.example.eiraj.seekdoers.Views.Fragments.NoConnectionFragment;
import com.example.eiraj.seekdoers.Views.Fragments.WentWrongFragment;
import com.facebook.login.LoginManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApiCallHandler,
        WentWrongFragment.OnFragmentInteractionListener,
        NoConnectionFragment.OnFragmentInteractionListener,
        LoadingFragment.OnFragmentInteractionListener{


    //================================================================================
    // Variables
    //================================================================================

    private static final int FILTER_REQUEST_CODE = 123;

    GetCategories getCategories;

    RecyclerView recyclerView;

    List<Category> categoryList;

    HomeAdapter homeAdapter;

    LoadingFragment loadingFragment;
    WentWrongFragment wentWrongFragment;
    NoConnectionFragment noConnectionFragment;
    SharedPreferences sharedPreferences;
    public static boolean isLoggedIn;
    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.homeFilterButton) {

            //new FilterDialog(ListingsActivity.this).show();
            Intent intent = new Intent( this , FilterActivity.class );
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("callerActivity" , "HomeActivity");
            intent.putExtra("latitude" , 43.757711);
            intent.putExtra("longitude" , -79.2285656);
            intent.putExtra("tagList" , new ArrayList<Tag>());
            intent.putExtra("radius" , 50);
            intent.putExtra("cityName" , "Mississauga");
            intent.putExtra("subCategoryID" , 0);
            intent.putExtra("subCategoryName" , "All");
            intent.putExtra("keyword" , "");
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


        } else if (id == R.id.profile) {
            CommonFunctions.updateProfile(this);

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

    private void initVariables()
    {
        categoryList = new ArrayList<>();
    }

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

        loadingFragment = new LoadingFragment();
        wentWrongFragment = new WentWrongFragment();
        noConnectionFragment = new NoConnectionFragment();
        recyclerView = (RecyclerView) findViewById(R.id.home_activity_recycle_view);

    }

    private void initRecyclerView()
    {

        homeAdapter = new HomeAdapter(this , categoryList, new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {

                Intent intent = new Intent(getApplicationContext() , SubCategoryActivity.class);
                intent.putExtra("categoryName" , category.getJob_category());
                intent.putExtra("categoryID" , category.getTerm_id());
                startActivity(intent);

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(homeAdapter);

    }

    private void showData ()
    {

        getSupportFragmentManager().beginTransaction().add(R.id.home_activity_frame_layout,loadingFragment).commit();
        getCategories = new GetCategories(this);

    }



    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {

        categoryList.addAll(getCategories.categoryList);

        recyclerView.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();

        homeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onApiFailure(Exception c) {

        c.printStackTrace();
        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.home_activity_frame_layout,wentWrongFragment).commit();

    }

    @Override
    public void onNoConnection() {

        getSupportFragmentManager().beginTransaction().remove(loadingFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.home_activity_frame_layout,noConnectionFragment).commit();

    }



    //================================================================================
    // Fragment Handler Methods
    //================================================================================

    @Override
    public void onWentWrongTryAgainButtonClicked() {

        getSupportFragmentManager().beginTransaction().remove(wentWrongFragment).commit();
        showData();

    }

    @Override
    public void onNoConnectionTryAgainButtonClicked() {

        getSupportFragmentManager().beginTransaction().remove(noConnectionFragment).commit();
        showData();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNoConnectionFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLoadingFragmentInteraction(Uri uri) {

    }





}
