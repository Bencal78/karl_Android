package com.example.karl.karl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.karl.karl.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import static com.google.android.gms.common.api.GoogleApiClient.getAllClients;

public class Parameters extends AppCompatActivity {
    ImageButton buttonCloset;
    ImageButton buttonOotd;
    ImageButton buttonSaved;
    ImageButton buttonParams;
    Button signout;
    private GoogleSignInAccount mGoogleSignInClient;
    private ParameterCallback mParameterCallback;
    private Context mContext;
    private Activity activity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameters);



        mGoogleSignInClient = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        signout = findViewById(R.id.signout);

        buttonCloset = findViewById(R.id.buttonCloset);
        buttonOotd = findViewById(R.id.buttonOotd);
        buttonSaved = findViewById(R.id.buttonSaved);
        buttonParams = findViewById(R.id.buttonParams);

        buttonCloset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Parameters.this, ClotheList.class);
                Parameters.this.startActivity(myIntent);
            }
        });
        buttonOotd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Parameters.this, Ootd.class);
                Parameters.this.startActivity(myIntent);
            }
        });
        buttonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Parameters.this, SavedOutfits.class);
                Parameters.this.startActivity(myIntent);
            }
        });
        mContext = this;
        activity = this;
        //Instantiate with Login static context
        mParameterCallback = (ParameterCallback) Login.getContext();


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Callback function in Login
                mParameterCallback.signOut();
            }
        });



    }

    public interface ParameterCallback{
        void signOut();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }



}
