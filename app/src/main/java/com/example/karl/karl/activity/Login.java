package com.example.karl.karl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.karl.karl.R;
import com.example.karl.karl.adapter.CardAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Model;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetGoogleDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.GoogleRetrofitInstance;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Edouard on 19/09/2018.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="Sign In Activity " ;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    Animation animation;
    ImageView logokarl;
    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String serverClientId = getResources().getString(R.string.google_server_client_id);

        setContentView(R.layout.login);
        requestQueue = Volley.newRequestQueue(this);  // This setups up a new request queue which we will need to make HTTP requests.

        //creation du bouton google+

        logokarl = findViewById(R.id.logo);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        //demande de l'email a l'utilisateur
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestScopes(new Scope(Scopes.PLUS_ME))
                //.requestServerAuthCode(getString(R.string.google_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
        //updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account!=null)
        {
            Intent myIntent = new Intent(Login.this, Welcome_Page.class);
            startActivity(myIntent);
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

            Log.e("account ", account.getId());
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
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(account.getId());
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, retrofit2.Response<ArrayList<User>> response) {
                try {
                    if(response.body().size() == 0){
                        getGoogleInfo(account);
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
        Call<JsonElement> call = service.getPeople(account.getId(), "genders,ageRanges", getString(R.string.google_api_keys));

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                try {
                    Log.e("response", response.body().toString());
                    JSONObject obj = new JSONObject(response.body().getAsJsonObject().toString());
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


}