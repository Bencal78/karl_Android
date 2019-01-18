package com.example.karl.karl.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.OotdAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Outfit;
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

public class Ootd extends AppCompatActivity {
    private static String UserId = null;
    private static String Id =null ;
    String url;
    TextView tv3;
    TextView tv2;
    GridView gridView;

    public Ootd() throws IOException {
    }

    ImageView iv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ootd);
        iv = findViewById(R.id.logo);
        tv3 = findViewById(R.id.TextNothing);
        //tv2 = findViewById(R.id.texttest2);
        gridView = (GridView) findViewById(R.id.gridview);
        List<String> images = new ArrayList<String>();
        List<Integer> values  = new ArrayList<Integer>();


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        //Log.e("GoogleId ", GoogleId);
        GoogleIdToId(GoogleId);
        //Log.e("Id ", Id);


    }

    private void getOotd(final String id) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        GetPyreqDataService service = RetrofitInstance.getRetrofitInstance().create(GetPyreqDataService.class);
        Call<Outfit> call = service.getPyreq("return_outfit",id);
        Log.e("url", call.request().url() + "");

        call.enqueue(new Callback<Outfit>() {
            @Override
            public void onResponse(Call<Outfit> call, Response<Outfit> response) {
                String clothe_1id = null;
                try {
                    Log.e("ddd", response.body().toString());
                    ArrayList<String> clothesid = new ArrayList<>();
                    if (response.body() != null) {
                        int len = response.body().getOutfit().size();
                        for (int i=0;i<len;i++){
                            clothesid.add(getString(R.string.base_url)+"uploads/"+response.body().getOutfit().get(i).getId()+".png");
                        }
                    }
                    OotdAdapter gridAdapter = new OotdAdapter(getApplicationContext(),clothesid);
                    gridView.setAdapter(gridAdapter);
                    tv3.setText("This is your Outfit of the Day !");
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
                getOotd("5c35d55fcba93d1c8d350204");

            }
            catch (Exception e) {
                tv3.setText("You have an issue with your user : Lisa par default");
                getOotd("5c35d55fcba93d1c8d350204");
            }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("googleIdToId error", t.toString());

            }
        });
    }
}