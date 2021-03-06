package com.example.karl.karl.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.OutfitsAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.SavedOutfitImage;
import com.example.karl.karl.model.Taste;
import com.example.karl.karl.model.User;
import com.example.karl.karl.model.UserClothe;
import com.example.karl.karl.model.UserOutfit;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteSavedOutfits extends AppCompatActivity implements OutfitsAdapter.GalleryAdapterCallBacks {
    //Deceleration of list of  GalleryItems
    public List<SavedOutfitImage> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    OutfitsAdapter mGalleryAdapter;
    private ImageView settings_button;
    private User user;
    private int isClicked = 0;
    private AlertDialog _dialog;
    private Boolean isSet = false;
    private Context mContext;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_saved_outfits);
        mContext = getApplicationContext();
        //setup RecyclerView
        RecyclerView recyclerViewGallery = findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
        //Create RecyclerView Adapter
        mGalleryAdapter = new OutfitsAdapter(this);
        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);
        //check for read storage permission
        //get images
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        getUser(GoogleId);

        Button buttonSave = findViewById(R.id.boutonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClothes(user);
            }
        });
    }

    private void getUser(String googleId) {
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(googleId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                try {
                    user = response.body().get(0);
                    galleryItems = new ArrayList<>();
                    for(int i=0; i< user.getTastes().size(); i++){
                        if(user.getTastes().get(i).getDecision()){
                            ArrayList<String> imageUri = new ArrayList<>();
                            for(int j=0; j<user.getTastes().get(i).getClothes().size(); j++){
                                imageUri.add(getString(R.string.base_url)+"uploads/"+user.getTastes().get(i).getClothes().get(j).getId()+".png");
                            }
                            galleryItems.add(new SavedOutfitImage(imageUri, user.getTastes().get(i).getId()));

                        }
                    }
                    mGalleryAdapter.addGalleryItems(galleryItems);
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

    private void saveClothes(User user) {
        ArrayList<Taste> selectedOutfits = new ArrayList<>();
        for(int i=0; i<galleryItems.size(); i++){
            if((galleryItems.get(i).isSelected))
            {
                //Log.e("ok ", "ok ici selected");
                for(int j=0; j<user.getTastes().size(); j++){
                    //Log.e("user taste id", user.getTastes().get(j).getId());
                    //Log.e("gallery taste id", galleryItems.get(i).imageName);
                    if(user.getTastes().get(j).getId().equals(galleryItems.get(i).imageName)){
                        selectedOutfits.add(user.getTastes().get(j));
                    }
                }
            }
        }
        UserOutfit userOutfits = new UserOutfit(user.getId(), selectedOutfits);
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<JsonElement> call = service.deleteOutfit(userOutfits);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Log.e("response", response.body().toString());
                updateUI();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
            }
        });
    }

    private void updateUI() {
        if(user.getTastes().size() == 0){
            Intent myIntent = new Intent(DeleteSavedOutfits.this, WelcomeQuiz.class);
            startActivity(myIntent);
        }
        else{
            Intent myIntent = new Intent(DeleteSavedOutfits.this, SavedOutfits.class);
            startActivity(myIntent);
        }
    }


    @Override
    public void onItemSelected(int position) {
        isClicked += 1;
        if(isClicked % 2 == 0){
            galleryItems.get(position).isSelected = !galleryItems.get(position).isSelected;
            galleryItems.get(position).checkBox.setChecked(galleryItems.get(position).isSelected);
        }
    }

    @Override
    public void onReleaseItem(int position) {
        _dialog.cancel();
        isSet = false;
    }

    @Override
    public void onItemLongSelected(int position) {
        if(!isSet){
            AlertDialog.Builder alertadd = new AlertDialog.Builder(this);
            LayoutInflater factory = LayoutInflater.from(this);
            final View view = factory.inflate(R.layout.modal_view, null);

            SavedOutfitImage outfit = galleryItems.get(position);
            ArrayList<ImageView> images = new ArrayList<>();

            switch(outfit.imageUri.size()){
                case 0:
                    break;
                case 1:
                    //Load with Picasso
                    images.add((ImageView) view.findViewById(R.id.Outfit1ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(0))
                            .into(images.get(0));

                    break;
                case 2:
                    //Load with Picasso
                    images.add((ImageView) view.findViewById(R.id.Outfit1ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(0))
                            .into(images.get(0));

                    images.add((ImageView) view.findViewById(R.id.Outfit2ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(1))
                            .into(images.get(1));
                    break;
                case 3:
                    //Load with Picasso
                    images.add((ImageView) view.findViewById(R.id.Outfit1ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(0))
                            .into(images.get(0));

                    images.add((ImageView) view.findViewById(R.id.Outfit2ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(1))
                            .into(images.get(1));

                    images.add((ImageView) view.findViewById(R.id.Outfit3ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(2))
                            .into(images.get(2));
                    break;
                case 4:
                    //Load with Picasso
                    images.add((ImageView) view.findViewById(R.id.Outfit1ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(0))
                            .into(images.get(0));

                    images.add((ImageView) view.findViewById(R.id.Outfit2ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(1))
                            .into(images.get(1));

                    images.add((ImageView) view.findViewById(R.id.Outfit3ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(2))
                            .into(images.get(2));

                    images.add((ImageView) view.findViewById(R.id.Outfit4ImageView));

                    Picasso.with(mContext)
                            .load(outfit.imageUri.get(3))
                            .into(images.get(3));
                    break;
            }
            alertadd.setView(view);

            _dialog = alertadd.show();
            isSet = true;
        }
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
}
