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
    String BASE_URL = "http://18.184.156.66:8000/";
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
        tv3 = findViewById(R.id.textView);
        //tv2 = findViewById(R.id.texttest2);
        gridView = (GridView) findViewById(R.id.gridview);
        List<String> images = new ArrayList<String>();
        List<Integer> values  = new ArrayList<Integer>();

        OotdAdapter gridAdapter = new OotdAdapter(this,values,images);
        gridView.setAdapter(gridAdapter);
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
        Call<JsonElement> call = service.getPyreq("return_outfit",id);
        Log.e("url", call.request().url() + "");

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                String clothe_1id = null;
                try {
                    Log.e("ddd", response.body().toString());
                    ArrayList<Clothe> clothes = new ArrayList<>();
                    if (response.body().getAsJsonObject().get("outfit").getAsJsonArray() != null) {
                        int len = response.body().getAsJsonObject().get("outfit").getAsJsonArray().size();
                        for (int i=0;i<len;i++){
                            clothes.add(new Clothe(new JSONObject(response.body().getAsJsonObject().get("outfit").getAsJsonArray().get(i).toString())));
                        }
                    }
                    Log.e("clothes", clothes.toString());
                    //clothe_1id = response.body().getString(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //tv3.setText(clothe_1id);
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
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
                assert response.body() != null;
                UserId = response.body().get(0).getId();
                getOotd(UserId);
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("googleIdToId error", t.toString());
            }
        });
    }
}