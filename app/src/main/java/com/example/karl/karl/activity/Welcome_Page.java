package com.example.karl.karl.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.karl.karl.DownloadImageFromInternet;
import com.example.karl.karl.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Edouard on 19/09/2018.
 */

@SuppressLint("Registered")
public class Welcome_Page extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        ProgressBar spinner;

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        Button buttonquiz = findViewById(R.id.boutonquizz);
        buttonquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Welcome_Page.this, QuizStart.class);
                startActivity(myIntent);
            }
        });

        // Utiliser la couche matérielle

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.persons_name);
        TextView tv2 = (TextView) findViewById(R.id.invitation);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            String personPhoto = String.valueOf(acct.getPhotoUrl());
            CircleImageView imageView = findViewById(R.id.persons_face);

            tv.setText("Bonjour " + personGivenName);
            tv2.setText("Nous allons commencer par un petit quizz pour que j'apprenne à mieux te connaitre." +
                    " Indique moi si les tenues te plaisent");

            if(personPhoto != "null"){
                new DownloadImageFromInternet(imageView,spinner)
                        .execute(personPhoto);
            }
            else{
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.avatar_circle_blue_120dp));
                spinner.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void updateUI(GoogleSignInAccount account) {
    }

}
