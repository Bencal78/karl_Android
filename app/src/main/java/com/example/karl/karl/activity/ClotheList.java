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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.ClotheAdapter;
import com.example.karl.karl.model.ClotheImage;
import com.example.karl.karl.model.SavedOutfitImage;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClotheList extends AppCompatActivity implements ClotheAdapter.GalleryAdapterCallBacks {
    //Deceleration of list of  GalleryItems
    public List<ClotheImage> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    ClotheAdapter mGalleryAdapter;
    BottomNavigationView mbotomnavcloth;
    private ImageView settings_button;
    private AlertDialog _dialog;
    private Boolean isSet = false;
    private Context mContext;
    ImageButton buttonCloset;
    ImageButton buttonOotd;
    ImageButton buttonSaved;
    ImageButton buttonParams;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothe_list);
        mContext = getApplicationContext();
        //setup RecyclerView
        RecyclerView recyclerViewGallery = findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
        //Create RecyclerView Adapter
        mGalleryAdapter = new ClotheAdapter(this);

        //MENU BAS
        buttonCloset = findViewById(R.id.buttonCloset);
        buttonOotd = findViewById(R.id.buttonOotd);
        buttonSaved = findViewById(R.id.buttonSaved);
        buttonParams = findViewById(R.id.buttonParams);

        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);
        //check for read storage permission
        //get images
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        getUser(GoogleId);

        buttonOotd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ClotheList.this, Ootd.class);
                ClotheList.this.startActivity(myIntent);
            }
        });
        buttonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ClotheList.this, SavedOutfits.class);
                ClotheList.this.startActivity(myIntent);
            }
        });
        buttonParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ClotheList.this, Parameters.class);
                ClotheList.this.startActivity(myIntent);
            }
        });


        settings_button = (ImageView) findViewById(R.id.settings_popup_menu);
        settings_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ClotheList.this, settings_button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.settings_popup_clothes, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.addClothes:
                                Intent myIntent = new Intent(ClotheList.this, AddClotheList.class);
                                startActivity(myIntent);
                                break;
                            case R.id.deleteClothes:
                                Intent myIntent2 = new Intent(ClotheList.this, DeleteClotheList.class);
                                startActivity(myIntent2);
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method
    }

    private void getUser(String googleId) {
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(googleId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                try {
                    User user = response.body().get(0);
                    galleryItems = new ArrayList<>();
                    for(int i=0; i< user.getClothes().size(); i++){
                        galleryItems.add(new ClotheImage(getString(R.string.base_url)+"uploads/"+user.getClothes().get(i).getId()+".png", user.getClothes().get(i).getName()));
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


    @Override
    public void onItemSelected(int position) {

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
            final View view = factory.inflate(R.layout.modal_view_clothe, null);

            ClotheImage clothe_image = galleryItems.get(position);
            ImageView image = view.findViewById(R.id.image_clothe);
            Picasso.with(mContext)
                    .load(clothe_image.imageUri)
                    .into(image);

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
