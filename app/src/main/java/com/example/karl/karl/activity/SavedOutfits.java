package com.example.karl.karl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.karl.karl.R;

public class SavedOutfits extends AppCompatActivity {
    BottomNavigationView mbotomnavsaved;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_outfits);

        mbotomnavsaved = findViewById(R.id.bottom_navigation_saved);


       mbotomnavsaved.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_ongle_1:
                        Intent myIntent = new Intent(SavedOutfits.this, ClotheList.class);
                        startActivity(myIntent);
                        break;
                    case R.id.action_ongle_2:
                        Intent myIntent2 = new Intent(SavedOutfits.this, Ootd.class);
                        startActivity(myIntent2);
                        break;
                }
                return true;
            }
        });

    }

}
