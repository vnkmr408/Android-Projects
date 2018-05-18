package com.example.eiraj.seekdoers.Views.Activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eiraj.seekdoers.Adapters.HomeAdapter;
import com.example.eiraj.seekdoers.Adapters.SpinnerAdapter;
import com.example.eiraj.seekdoers.Adapters.TagsAdapter;
import com.example.eiraj.seekdoers.ApiCalls.GetTagList;
import com.example.eiraj.seekdoers.Modal.Category;
import com.example.eiraj.seekdoers.Modal.Tag;
import com.example.eiraj.seekdoers.R;
import com.example.eiraj.seekdoers.Utils.ApiCallHandler;
import com.example.eiraj.seekdoers.Utils.NonScrollListView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ApiCallHandler {

    //================================================================================
    // Variables
    //================================================================================

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 123;
    private static final int SELECT_CATEGORY_REQUEST_CODE = 124;
    private static final int LISTINGS_REQUEST_CODE = 125;
    private final static int FILTER_REQUEST_CODE = 101;

    String callerActivity;

    Place selectedPlace;

    SeekBar radiusSeekBar;
    TextView seekBarValue;

    double latitude;
    double longitude;
    int radius;
    String cityName;
    int categoryID;
    String categoryName;
    String keyword;

    LinearLayout tagsLoadingLayout;

    NonScrollListView filterRecyclerView;
    EditText cityEditText;
    EditText categoryEditText;
    EditText keywordEditText;

    Button applyButton;
    TextView titleTextView;

    TagsAdapter tagsAdapter;

    GetTagList getTagList;

    ArrayList<Tag> tagList;

    private GoogleApiClient mGoogleApiClient;


    //================================================================================
    // Activity Methods
    //================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

//        setTitle("Filters");


        initGUI();
        initVariables();
        initCitySearch();
        initRadius();
        initRecycler();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE)
        {

            Place place = PlacePicker.getPlace( data , this );
            cityEditText.setText(place.getName());
            selectedPlace = place;

        }

        if (requestCode == SELECT_CATEGORY_REQUEST_CODE )
        {

            if(resultCode == RESULT_OK)
            {

                categoryName = data.getExtras().getString("categoryName");
                categoryEditText.setText(categoryName);
                categoryID = data.getExtras().getInt("categoryID");

            }

        }

        if(requestCode == LISTINGS_REQUEST_CODE)
        {

            if(requestCode == RESULT_CANCELED)
            {
                setResult(RESULT_CANCELED , null);
                finish();
            }

        }

    }



    //================================================================================
    // Initializing Methods
    //================================================================================

    private void initCitySearch()
    {

        cityEditText = (EditText) findViewById(R.id.cityEditText);
        cityEditText.setText(cityName);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        findViewById(R.id.cityView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {

                    AutocompleteFilter filter = new AutocompleteFilter.Builder()
                            .setCountry("CA")
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                            .build();

                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(filter)
                            .build(FilterActivity.this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void initRadius()
    {

        seekBarValue = (TextView) this.findViewById(R.id.seek_bar_value);

        radiusSeekBar = (SeekBar) this.findViewById(R.id.radius_seek_bar );

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText( seekBar.getProgress() + " KM" );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radiusSeekBar.setProgress(radius);

    }

    private void initGUI()
    {

        keywordEditText = (EditText) findViewById(R.id.keywordEditText);

        keywordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = String.valueOf(s);
            }
        });

        titleTextView = (TextView) findViewById(R.id.filtersTitleTextView);

        applyButton = (Button) this.findViewById(R.id.filter_apply_button);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callerActivity.compareTo("HomeActivity") == 0)
                {

                    Intent intent = new Intent(FilterActivity.this , ListingsActivity.class );
                    intent.putExtra("callerActivity" , callerActivity);
                    intent.putExtra("subCategoryName" , categoryName );
                    intent.putExtra("subCategoryID" , categoryID );
                    intent.putExtra("latitude" , latitude);
                    intent.putExtra("longitude" , longitude);
                    intent.putExtra("tagList" , tagList);
                    intent.putExtra("radius" , radius);
                    intent.putExtra("cityName" , cityName);
                    intent.putExtra("keyword" , keyword);
                    startActivityForResult( intent , LISTINGS_REQUEST_CODE );

                }
                else {

                    Intent intent = new Intent();
                    intent.putExtra("radius", radiusSeekBar.getProgress());

                    if (selectedPlace != null) {
                        intent.putExtra("latitude", selectedPlace.getLatLng().latitude);
                        intent.putExtra("longitude", selectedPlace.getLatLng().longitude);
                        intent.putExtra("cityName", selectedPlace.getName());
                    } else {
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("cityName", cityName);
                    }
                    intent.putExtra("tags", tagList);
                    intent.putExtra("keyword" , keyword);
                    intent.putExtra("subCategoryID" , categoryID);
                    intent.putExtra("subCategoryName" , categoryName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    void initVariables()
    {

        callerActivity = getIntent().getExtras().getString("callerActivity");

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        tagList = getIntent().getExtras().getParcelableArrayList("tagList");
        radius = getIntent().getExtras().getInt("radius");
        cityName = getIntent().getExtras().getString("cityName");
        categoryID = getIntent().getExtras().getInt("subCategoryID");
        categoryName = getIntent().getExtras().getString("subCategoryName");
        keyword = getIntent().getExtras().getString("keyword");

        keywordEditText.setText(keyword);

        if(callerActivity.contains("HomeActivity") )
        {
            initCategroy();
            if(callerActivity.compareTo("HomeActivity") == 0) {
                titleTextView.setText("FIND");
                applyButton.setText("SEARCH");
            }
        }
        else
        {


        }

        tagsLoadingLayout = (LinearLayout) findViewById(R.id.tagsLoadingLayout);

        findViewById(R.id.backButtonFilterActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED , null);
                finish();

            }
        });

    }

    void initRecycler()
    {

        filterRecyclerView = (NonScrollListView) findViewById(R.id.filterRecyclerView);
        tagsAdapter = new TagsAdapter(tagList);

        if(tagList.size() == 0)
            getTagList = new GetTagList(this);
        else
            tagsLoadingLayout.setVisibility(View.GONE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        filterRecyclerView.setLayoutManager(mLayoutManager);
        filterRecyclerView.setItemAnimator(new DefaultItemAnimator());
        filterRecyclerView.setAdapter(tagsAdapter);

    }

    void initCategroy()
    {

        categoryEditText = (EditText) findViewById(R.id.categoryEditText);
        categoryEditText.setText(categoryName);

        findViewById(R.id.categoryFilterFrame).setVisibility(View.VISIBLE);

        findViewById(R.id.categoryView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FilterActivity.this , SelectCategoryAcitivity.class);
                startActivityForResult( intent , SELECT_CATEGORY_REQUEST_CODE );

            }
        });


    }


    //================================================================================
    // Api Handler Methods
    //================================================================================

    @Override
    public void onApiSuccess() {

        tagsLoadingLayout.setVisibility(View.GONE);

        tagList.addAll(getTagList.tagList);

        tagsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onApiFailure(Exception c) {

    }

    @Override
    public void onNoConnection() {

    }
}
