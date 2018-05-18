package com.example.eiraj.seekdoers.Views.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.eiraj.seekdoers.Adapters.CommentsAdapter;
import com.example.eiraj.seekdoers.Adapters.SelectCategoryAdapter;
import com.example.eiraj.seekdoers.ApiCalls.GetSubCategory;
import com.example.eiraj.seekdoers.Modal.SubCategory;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryAcitivity extends AppCompatActivity implements ApiCallHandler {


    //================================================================================
    // Variables
    //================================================================================

    GetSubCategory getSubCategory;

    List<SubCategory> subCategoryList;

    SearchView selectCategorySearchView;
    RecyclerView selectCategoryRecyclerView;
    ProgressBar selectCategoryProgressBar;

    SelectCategoryAdapter selectCategoryAdapter;


    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category_acitivity);

        initVariable();
        initGUI();
        initGUIbehaviour();
        initRecycler();

    }

    //================================================================================
    // Initializing Methods
    //================================================================================

    private void initVariable()
    {

        subCategoryList = new ArrayList<>();
        getSubCategory = new GetSubCategory(this , 0);

    }

    private void initGUI()
    {

        selectCategorySearchView = (SearchView) findViewById(R.id.selectCategorySearch);
        selectCategoryProgressBar = (ProgressBar) findViewById(R.id.selectCategoryProgressBar);
        selectCategoryRecyclerView = (RecyclerView) findViewById(R.id.selectCategoryRecyclerView);

    }

    private void initGUIbehaviour ()
    {

        selectCategorySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                selectCategoryAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                selectCategoryAdapter.filter(newText);
                return false;
            }
        });

        selectCategorySearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                selectCategoryAdapter.filter("");
                return false;
            }
        });

    }

    private void initRecycler()
    {

        selectCategoryAdapter = new SelectCategoryAdapter(subCategoryList, new SelectCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SubCategory subCategory) {

                Intent intent = new Intent();
                intent.putExtra("categoryName" , subCategory.getJob_category());
                intent.putExtra("categoryID" , subCategory.getTerm_id());
                setResult( RESULT_OK , intent );
                finish();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        selectCategoryRecyclerView.setLayoutManager(mLayoutManager);
        selectCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        selectCategoryRecyclerView.setAdapter(selectCategoryAdapter);

    }


    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {

        selectCategoryProgressBar.setVisibility(View.GONE);
        subCategoryList.addAll(getSubCategory.subCategoryList);
        selectCategoryAdapter.subCategoryList.addAll(getSubCategory.subCategoryList);
        selectCategoryAdapter.notifyDataSetChanged();

    }

    @Override
    public void onApiFailure(Exception c) {

    }

    @Override
    public void onNoConnection() {

    }
}
