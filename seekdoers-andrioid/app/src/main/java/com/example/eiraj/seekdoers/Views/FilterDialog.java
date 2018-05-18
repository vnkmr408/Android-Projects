package com.example.eiraj.seekdoers.Views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eiraj.seekdoers.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 08-08-2017.
 */

public class FilterDialog extends Dialog {

    private Spinner citySpinner;
    private ArrayAdapter<String> cityAdapter;
    private List<String> cities;

    private Context context;

    public FilterDialog(@NonNull Context context) {

        super(context);
        this.context = context;
        initData();
        initDialog();
        configSpinners();
    }

    private void initDialog()
    {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.filter_dialog);
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = this.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.RIGHT;

        this.getWindow().setAttributes(wmlp);

    }

    private void initData()
    {

        cities = new ArrayList<>();
        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add("Kolkata");
        cities.add("Chennai");
    }

    private void configSpinners()
    {
        cityAdapter = new ArrayAdapter<String>(context , android.R.layout.simple_spinner_item , cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySpinner = (Spinner) this.findViewById(R.id.citySpinner);
        citySpinner.setAdapter(cityAdapter);
    }
}
