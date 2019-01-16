package com.example.karl.karl.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.ClotheAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.my_interface.GetClotheDataService;
import com.example.karl.karl.network.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    private ClotheAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        /** Create handle for the RetrofitInstance interface*/
        GetClotheDataService service = RetrofitInstance.getRetrofitInstance().create(GetClotheDataService.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ArrayList<Clothe>> call = service.getClotheById("5c33759bf946f80646b32d08");
        //Call<ArrayList<Clothe>> call = service.getClothe();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<Clothe>>() {
            @Override
            public void onResponse(Call<ArrayList<Clothe>> call, Response<ArrayList<Clothe>> response) {
                Log.e("size", String.valueOf(response.body().size()));
                Log.e("response", response.body().get(0).getName());
                //generateClotheList(response.body().getClotheArrayList());
            }

            @Override
            public void onFailure(Call<ArrayList<Clothe>> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Method to generate List of notice using RecyclerView with custom adapter*/
    private void generateClotheList(ArrayList<Clothe> clotheArrayList) {
        /*
        recyclerView = findViewById(R.id.recycler_view_notice_list);
        adapter = new ClotheAdapter(clotheArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        */
    }

}
