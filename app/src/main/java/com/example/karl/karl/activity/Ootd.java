package com.example.karl.karl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Edouard on 20/09/2018.
 */


public class Ootd extends AppCompatActivity implements TinderCard.TinderCallback {
    private static String UserId = null;
    private static String Id =null ;
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    String url;
    TextView tv3;
    TextView tv2;
    GridView gridView;
    ImageView imagesoleil;
    ImageView imagecal;
    String id;
    BottomNavigationView mbotomnav;


    public Ootd() throws IOException {
    }

    ImageView iv;

    @SuppressLint("ResourceAsColor")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ootd);
        iv = findViewById(R.id.logo);
        tv3 = findViewById(R.id.TextNothing);
        imagesoleil = findViewById(R.id.imgsoleil);
        imagecal = findViewById(R.id.imgcal);
        mbotomnav = findViewById(R.id.bottom_navigation);

        mSwipeView = findViewById(R.id.swipeView);
        mContext = this;

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
        List<String> images = new ArrayList<String>();
        List<Integer> values  = new ArrayList<Integer>();
        imagesoleil.setImageDrawable(getResources().getDrawable(R.drawable.lesoleil));
        imagecal.setImageDrawable(getResources().getDrawable(R.drawable.calendrier));


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

        id = getId();
        //id = "5c35d55fcba93d1c8d350204";
        GetPyreqDataService service = RetrofitInstance.getRetrofitInstance().create(GetPyreqDataService.class);
        Call<Outfit> call = service.getPyreq("return_outfit",id);
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
                        mSwipeView.addView(new TinderCard(mContext, taste, mSwipeView,id));
                    }

                    tv3.setText("Outfit of the Day !");
                } catch (Exception e ) {
                    e.printStackTrace();
                    tv3.setText("You have no outfits");
                }
            }

            @Override
            public void onFailure(Call<Outfit> call, Throwable t) {
                Log.e("getOotd error", t.toString());

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
}