package com.example.karl.karl.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.ClotheAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetClotheDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClotheList extends AppCompatActivity implements ClotheAdapter.GalleryAdapterCallBacks{
    //Deceleration of list of  GalleryItems
    public List<ClotheImage> galleryItems;
    //Read storage permission request code
    private static final int RC_READ_STORAGE = 5;
    ClotheAdapter mGalleryAdapter;
    private ArrayList<Boolean> itemChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_clothe_list);
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
        String GoogleId = String.valueOf(acct.getId());
        getClohes();
    }

    private void getClohes() {
        GetClotheDataService service = RetrofitInstance.getRetrofitInstance().create(GetClotheDataService.class);
        Call<ArrayList<Clothe>> call = service.getClothe();

        call.enqueue(new Callback<ArrayList<Clothe>>() {
            @Override
            public void onResponse(Call<ArrayList<Clothe>> call, Response<ArrayList<Clothe>> response) {
                try {

                    ArrayList<Clothe> clothes = response.body();
                    galleryItems = new ArrayList<>();
                    itemChecked = new ArrayList<>();
                    for(int i=0; i< clothes.size(); i++){
                        galleryItems.add(new ClotheImage(getString(R.string.base_url)+"uploads/"+clothes.get(i).getId()+".png", clothes.get(i).getName()));
                        itemChecked.add(i, false);
                    }
                    mGalleryAdapter.addGalleryItems(galleryItems);
                }
                catch (Exception e) {
                    Log.e("exception in getUser", e.toString());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Clothe>> call, Throwable t) {
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
        //Log.e("position", String.valueOf(position));
        itemChecked.set(position, !itemChecked.get(position));
        //Log.e("selected", String.valueOf(galleryItems.get(position).checkBox.isChecked()));
        //galleryItems.get(position).checkBox.setChecked(itemChecked.get(posi) == null ? false : checkedState);

        //galleryItems.get(position).checkBox.setChecked(itemChecked.get(position));
        //Log.e("size gallery", String.valueOf(galleryItems.size()));

        //galleryItems.get(position).checkBox.setChecked(!galleryItems.get(position).checkBox.isChecked());
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
