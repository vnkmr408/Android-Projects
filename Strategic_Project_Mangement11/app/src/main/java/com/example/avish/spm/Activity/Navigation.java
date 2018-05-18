package com.example.avish.spm.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.avish.spm.R;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MyInformation()).commit();
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
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AboutUs()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.myinfo) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MyInformation()).commit();
        } else if (id == R.id.addproject) {

            fragmentManager.beginTransaction().replace(R.id.content_frame, new AddProjectFragment()).commit();
        } else if (id == R.id.addemployee) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AddEmployeeFragment()).commit();
        } else if (id == R.id.projects) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProjectListing()).commit();

        } else if (id == R.id.members) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MemberListing()).commit();

        } else if (id == R.id.assignproject) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AssignProjectFragment()).commit();
        } else if (id == R.id.logout) {
            //When the user logged out,clear the sharedpreference and set application userid to zero
            sharedPreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.clear();
            editor.putInt("ApplicationUserId", 0);
            editor.commit();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
