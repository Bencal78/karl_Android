package com.example.karl.karl.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.ClotheAdapter;
import com.example.karl.karl.adapter.OotdAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Outfit;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetPyreqDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothe_list);
        //setup RecyclerView
        RecyclerView recyclerViewGallery = findViewById(R.id.recyclerViewGallery);
        recyclerViewGallery.setLayoutManager(new GridLayoutManager(this, 2));
        //Create RecyclerView Adapter
        mGalleryAdapter = new ClotheAdapter(this);
        //set adapter to RecyclerView
        recyclerViewGallery.setAdapter(mGalleryAdapter);
        //check for read storage permission
        //get images
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        getUser(GoogleId);
        mbotomnavcloth = findViewById(R.id.bottom_navigationCloth);

        mbotomnavcloth.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_ongle_2:
                        Intent myIntent = new Intent(ClotheList.this, Ootd.class);
                        startActivity(myIntent);
                        break;
                    case R.id.action_ongle_3:
                        Intent myIntent2 = new Intent(ClotheList.this, SavedOutfits.class);
                        startActivity(myIntent2);
                        break;
                }
                return true;
            }
        });

        settings_button = (ImageView) findViewById(R.id.settings_popup_menu);
        settings_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ClotheList.this, settings_button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.settings_popup, popup.getMenu());

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
        //create fullscreen SlideShowFragment dialog
        /*
        SlideShowFragment slideShowFragment = SlideShowFragment.newInstance(position);
        //setUp style for slide show fragment
        slideShowFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        //finally show dialogue
        slideShowFragment.show(getSupportFragmentManager(), null);
        */
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
