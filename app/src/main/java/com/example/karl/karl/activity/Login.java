package com.example.karl.karl.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.CardAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Model;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetGoogleDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.GoogleRetrofitInstance;
import com.example.karl.karl.network.RetrofitInstance;
import com.example.karl.karl.utils.PermissionUtils;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
//import com.google.android.gms.plus.Account;
//import com.google.android.gms.plus.People;
//import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Gender;
import com.google.api.services.people.v1.model.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Edouard on 19/09/2018.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, Parameters.ParameterCallback {
    private static final String TAG ="Sign In Activity " ;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    Animation animation;
    ImageView logokarl;
    private FirebaseAuth mAuth;
    private static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String serverClientId = getResources().getString(R.string.google_server_client_id);

        setContentView(R.layout.login);
        mContext = this;

        //creation du bouton google+

        logokarl = findViewById(R.id.logo);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        //demande de l'email a l'utilisateur
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.web_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN), new Scope(Scopes.PROFILE),
                        new Scope(Scopes.PLUS_ME), new Scope(CalendarScopes.CALENDAR_READONLY),
                        new Scope(CalendarScopes.CALENDAR))
                .requestEmail()
                .build();

        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                //logokarl.startAnimation(animation);
                signIn();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Log.e("on start", String.valueOf(account));
        //updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account!=null)
        {
            GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
            Call<ArrayList<User>> call = service.getUserByGoogleId(account.getId());
            call.enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(Call<ArrayList<User>> call, retrofit2.Response<ArrayList<User>> response) {
                    try {
                        if(response.body().size() == 0){
                            Intent myIntent = new Intent(Login.this, Welcome_Page.class);
                            startActivity(myIntent);
                            Log.e("ok3", "ok3");
                        }
                        else if(response.body().size() == 1 && response.body().get(0).getClothes().size() == 0){
                            Intent myIntent = new Intent(Login.this, Welcome_Page.class);
                            startActivity(myIntent);
                            Log.e("ok2", "ok2");
                        }
                        else if(response.body().size() == 1 && response.body().get(0).getTastes().size() > 0){
                            Intent myIntent = new Intent(Login.this, Ootd.class);
                            startActivity(myIntent);
                            Log.e("ok2", "ok2");
                        }
                        else{
                            Intent myIntent = new Intent(Login.this, WelcomeQuiz.class);
                            startActivity(myIntent);
                            Log.e("ok1", "ok1");
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                    Log.e("error", t.toString());

                }
            });
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("handleresult", String.valueOf(account));
            //Log.e("handleresult string", account.getDisplayName());
            checkNewUser(account);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.e(TAG, e.toString());
            updateUI(null);
        }
    }

    private void checkNewUser(final GoogleSignInAccount account){
        Log.e("account annother", account.getId());
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(account.getId());
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, retrofit2.Response<ArrayList<User>> response) {
                try {
                    if(response.body().size() == 0){
                        getGoogleInfo(account);
                    }
                    if(response.body().size() == 1 && account.getFamilyName() != null  && response.body().get(0).getLastName() == null){
                        updateUser(account, response.body().get(0));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("error", t.toString());

            }
        });
    }

    private void updateUser(GoogleSignInAccount account, User user) {
        user.setFirstName(account.getDisplayName());
        user.setLastName(account.getFamilyName());
        user.setGivenName(account.getDisplayName());
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<JsonElement> call = service.updateUser(user);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Log.e("response", response.body().toString());
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(final User usr){
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<JsonElement> call = service.createUser(usr);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Log.e("response", response.body().toString());
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getGoogleInfo(final GoogleSignInAccount account){
        GetGoogleDataService service = GoogleRetrofitInstance.getRetrofitInstance().create(GetGoogleDataService.class);
        Call<JsonElement> call = service.getPeople(account.getId(), "genders,ageRanges,birthdays", getString(R.string.google_api_keys));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                try {
                    Log.e("response", String.valueOf(response.body()));
                    JSONObject obj = new JSONObject();
                    if(response.body() != null){
                        obj = new JSONObject(response.body().getAsJsonObject().toString());
                    }
                    JSONObject usr = new JSONObject();
                    usr.put("idGoogle", account.getId());
                    usr.put("firstName", account.getGivenName());
                    usr.put("lastName", account.getFamilyName());
                    usr.put("givenName", account.getDisplayName());
                    usr.put("email", account.getEmail());
                    if(obj.has("gender")){usr.put("gender", obj.getString("gender"));}
                    createUser(new User(usr));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }

    @Override
    public void signOut() {

        Log.e("ok", "signout in login");
        mGoogleSignInClient.revokeAccess();
        /*
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
        */
        Intent myIntent = new Intent(Login.this, Login.class);
        startActivity(myIntent);
    }

    public static Context getContext() {
        return mContext;
    }
}