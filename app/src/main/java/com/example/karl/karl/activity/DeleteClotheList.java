package com.example.karl.karl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.ClotheAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.User;
import com.example.karl.karl.model.UserClothe;
import com.example.karl.karl.my_interface.GetClotheDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteClotheList extends AppCompatActivity implements ClotheAdapter.GalleryAdapterCallBacks {
    //Deceleration of list of  GalleryItems
    public List<ClotheImage> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    ClotheAdapter mGalleryAdapter;
    private ArrayList<Clothe> clothes;
    private String GoogleId;
    private Context mContext;
    private User user;
    public Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_clothe_list);
        mContext = getApplicationContext();
        //setup RecyclerView
        RecyclerView recyclerViewGallery = findViewById(R.id.addRecyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
        //Create RecyclerView Adapter
        mGalleryAdapter = new ClotheAdapter(this);
        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);
        //check for read storage permission
        //get images
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        GoogleId = String.valueOf(acct.getId());
        getUser(GoogleId);

        buttonSave = findViewById(R.id.boutonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClothes(user);
            }
        });
    }

    private void saveClothes(User user) {
        ArrayList<Clothe> selectedClothes = new ArrayList<>();
        for(int i=0; i<clothes.size(); i++){
            if((galleryItems.get(i).isSelected))
            {
                selectedClothes.add(clothes.get(i));
            }
        }
        UserClothe userClothes = new UserClothe(user.getId(), selectedClothes);
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<JsonElement> call = service.deleteClothe(userClothes);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Log.e("response", response.body().toString());
                updateUI();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(mContext, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if(user.getTastes().size() == 0){
            Intent myIntent = new Intent(DeleteClotheList.this, WelcomeQuiz.class);
            startActivity(myIntent);
        }
        else{
            Intent myIntent = new Intent(DeleteClotheList.this, ClotheList.class);
            startActivity(myIntent);
        }

    }

    private void getClohes() {
        clothes = user.getClothes();
        galleryItems = new ArrayList<>();
        for(int i=0; i<clothes.size(); i++){
            galleryItems.add(new ClotheImage(getString(R.string.base_url)+"uploads/"+clothes.get(i).getId()+".png", clothes.get(i).getName()));
        }
        mGalleryAdapter.addGalleryItems(galleryItems);
    }


    @Override
    public void onItemSelected(int position) {
        galleryItems.get(position).isSelected = !galleryItems.get(position).isSelected;
        galleryItems.get(position).checkBox.setChecked(galleryItems.get(position).isSelected);
        int a = 0;
        for(int i =0;i<galleryItems.size();i++) {
            if(galleryItems.get(i).isSelected) {a++;}
        }
        buttonSave.setText("Delete " + a + " Clothes ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
        if (requestCode == RC_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Get images
                galleryItems = GalleryUtils.getImages(this);
                // add images to gallery recyclerview using adapter
                mGalleryAdapter.addGalleryItems(galleryItems);
            } else {
                Toast.makeText(this, "Storage Permission denied", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void getUser(String googleId) {
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(googleId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                try {
                    if(response != null && response.body() != null && response.body().get(0) != null){
                        user = response.body().get(0);
                        getClohes();
                    }
                }
                catch (Exception e) {
                    Log.e("exception in getUser", e.toString());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("googleIdToId error", t.toString());

            }
        });
    }
}
