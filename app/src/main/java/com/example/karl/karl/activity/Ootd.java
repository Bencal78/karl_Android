package com.example.karl.karl.activity;

import android.Manifest;
import android.annotation.SuppressLint;

import com.example.karl.karl.my_interface.GetGoogleDataService;
import com.example.karl.karl.network.GoogleRetrofitInstance;
import com.example.karl.karl.network.WeatherDownload;
import com.example.karl.karl.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.api.client.auth.oauth2.Credential;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.OotdAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Outfit;
import com.example.karl.karl.model.Taste;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetPyreqDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.util.ArrayList;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Edouard on 20/09/2018.
 */


public class Ootd extends AppCompatActivity implements TinderCard.TinderCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtils.PermissionResultCallback {
    private static String UserId = null;
    private static String Id =null ;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    String url;
    TextView tv3;
    ProgressBar progressBar;
    TextView errortxt;
    GridView gridView;
    TextView imagesoleil;
    ImageView imagecal;
    String id;
    BottomNavigationView mbotomnav;

    private static final String TAG = Ootd.class.getSimpleName();

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private final static int REQUEST_CHECK_SETTINGS = 2000;

    private Location mLastLocation;

    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;

    double latitude;
    double longitude;

    // list of permissions

    ArrayList<String> permissions=new ArrayList<>();
    PermissionUtils permissionUtils;

    boolean isPermissionGranted;

    Typeface weatherFont;

    public Ootd() throws IOException {
    }

    ImageView iv;

    @SuppressLint("ResourceAsColor")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ootd);

        permissionUtils=new PermissionUtils(Ootd.this);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionUtils.check_permission(permissions,"Need GPS permission for getting your location",1);

        iv = findViewById(R.id.logo);
        tv3 = findViewById(R.id.TextNothing);
        imagesoleil = findViewById(R.id.weather_icon);
        imagecal = findViewById(R.id.imgcal);
        mbotomnav = findViewById(R.id.bottom_navigation);

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        imagesoleil.setTypeface(weatherFont);

        errortxt = findViewById(R.id.errortxt);
        mSwipeView = findViewById(R.id.swipeView);
        mContext = this;
        progressBar = findViewById(R.id.progressBarootd);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
        //tv2 = findViewById(R.id.texttest2);

        buildGoogleApiClient();

        List<String> images = new ArrayList<String>();
        List<Integer> values  = new ArrayList<Integer>();
        imagecal.setImageDrawable(getResources().getDrawable(R.drawable.calendrier));

        imagecal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Ootd.this, Calendar_View.class);
                startActivity(myIntent);
            }
        });
        tv3.setText("Outfit of the Day !");


        imagesoleil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Ootd.this, Weather.class);
                Ootd.this.startActivity(myIntent);
            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        //Log.e("GoogleId ", GoogleId);

        GoogleIdToId(GoogleId);
        //Log.e("Id ", Id);

        mbotomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_ongle_1:
                    Intent myIntent = new Intent(Ootd.this, ClotheList.class);
                    startActivity(myIntent);
                    break;
                case R.id.action_ongle_3:
                    Intent myIntent2 = new Intent(Ootd.this, SavedOutfits.class);
                    startActivity(myIntent2);
                    break;
            }
                return true;
            }
        });

    }

    public void getOotd() {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        progressBar.setVisibility(View.VISIBLE);

        id = getId();
        //id = "5c35d55fcba93d1c8d350204";
        GetPyreqDataService service = RetrofitInstance.getRetrofitInstance().create(GetPyreqDataService.class);
        Call<Outfit> call = service.getOutfitRl(id, String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
        Log.e("url", call.request().url() + "");

        call.enqueue(new Callback<Outfit>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<Outfit> call, Response<Outfit> response) {
                try {
                    ArrayList<Clothe> Clothes = new ArrayList<Clothe>();
                    Taste taste;
                    if (response.body() != null) {
                        int len = response.body().getOutfit().size();
                        for (int i=0;i<len;i++){

                            Clothes.add(new Clothe(response.body().getOutfit().get(i)));
                        }

                        taste = new Taste(null,null,Clothes);
                        progressBar.setVisibility(View.INVISIBLE);
                        mSwipeView.addView(new TinderCard(mContext, taste, mSwipeView,id));
                    }

                } catch (Exception e ) {
                    e.printStackTrace();
                    errortxt.setText("You Don't Have enough clothe");
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onFailure(Call<Outfit> call, Throwable t) {
                Log.e("getOotd error", t.toString());
                errortxt.setText("Something Went Wrong :(");
                progressBar.setVisibility(View.INVISIBLE);
            }

        });
    }
    private void GoogleIdToId(String GoogleId){
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(GoogleId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
            try {
                UserId = response.body().get(0).getId();
                //getOotd(UserId);
                setId(UserId);
                getOotd();

            }
            catch (Exception e) {
                tv3.setText("You have an issue with your user : Lisa par default");
                //getOotd();
            }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("googleIdToId error", t.toString());

            }
        });
    }
    public void setId(String id) {
        Id = id;
    }
    public String getId() {
        return Id;
    }

    @Override
    public void newTinderCard() {
        Log.e("does it works?", "yes");
        getOotd();
    }

    private void getWeather() {
        GetPyreqDataService service = RetrofitInstance.getRetrofitInstance().create(GetPyreqDataService.class);
        Call<JsonElement> call = service.getWeather(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body() != null){
                    JSONObject details = null;
                    try {
                        JSONObject json = new JSONObject(String.valueOf(response.body()));
                        json = (JSONObject) json.get("android");

                        details = json.getJSONArray("weather").getJSONObject(0);

                        imagesoleil.setText(Html.fromHtml(WeatherDownload.setWeatherIcon(details.getInt("id"),
                                json.getJSONObject("sys").getLong("sunrise") * 1000,
                                json.getJSONObject("sys").getLong("sunset") * 1000)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here
                        getLocation();
                        if(mLastLocation != null){
                            getWeather();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Ootd.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    /**
     * Method to verify google play services on the device
     * */

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }


    // Permission check functions


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }




    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionGranted=true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to display the location on UI
     * */

    private void getLocation() {
        if (isPermissionGranted) {
            try
            {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }

        }

    }
}