package com.example.avish.spm.Employee_Information;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.avish.spm.Activity.Login;
import com.example.avish.spm.Adapter.MyPagerAdapter_Home;
import com.example.avish.spm.R;

public class EmployeeHomeScreen extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_screen);
        tabLayout = (TabLayout) findViewById(R.id.tablayout1);
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Strategic Project Management");
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("My Information"));
        tabLayout.addTab(tabLayout.newTab().setText("My Tasks"));
        MyPagerAdapter_Home adapter = new MyPagerAdapter_Home(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                sharedPreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.clear();
                editor.putInt("ApplicationUserId", 0);
                editor.commit();
                Intent intent = new Intent(EmployeeHomeScreen.this, Login.class);
                startActivity(intent);

                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
