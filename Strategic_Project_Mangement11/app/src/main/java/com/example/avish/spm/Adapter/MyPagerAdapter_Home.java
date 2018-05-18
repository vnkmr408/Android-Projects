package com.example.avish.spm.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.example.avish.spm.Activity.EmployeeloginMyInfo;
import com.example.avish.spm.Employee_Information.EmployeeloginMyTask;

/**
 * Created by Avish on 10-04-2017.
 */

public class MyPagerAdapter_Home extends FragmentStatePagerAdapter {
    private int tabcount;

    public MyPagerAdapter_Home(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount = tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                EmployeeloginMyInfo fragmentOne = new EmployeeloginMyInfo();
                return fragmentOne;
            case 1:
                EmployeeloginMyTask fragmentTwo = new EmployeeloginMyTask();
                return fragmentTwo;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
