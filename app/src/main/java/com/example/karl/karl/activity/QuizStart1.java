package com.example.karl.karl.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.OotdAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Outfit;
import com.example.karl.karl.model.Taste;
import com.example.karl.karl.model.User;
import com.example.karl.karl.my_interface.GetClotheDataService;
import com.example.karl.karl.my_interface.GetPyreqDataService;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizStart1 extends AppCompatActivity {
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_start);

        mSwipeView = findViewById(R.id.swipeView);
        mContext = this;

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        //Log.e("GoogleId ", GoogleId);
        getUser(GoogleId);

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        Log.e("swipe resolver size", String.valueOf(mSwipeView.getAllResolvers().size()));
    }

    private void getUser(String googleId) {
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<ArrayList<User>> call = service.getUserByGoogleId(googleId);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                try {
                    User usr = response.body().get(0);
                    generateOutfits(usr);
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

    private void generateOutfits(final User usr) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        GetPyreqDataService service = RetrofitInstance.getRetrofitInstance().create(GetPyreqDataService.class);
        for(int i=0; i<10; i++){
            Call<Outfit> call = service.getOutfit(usr.getId());
            Log.e("url", call.request().url() + "");

            call.enqueue(new Callback<Outfit>() {
                @Override
                public void onResponse(Call<Outfit> call, Response<Outfit> response) {
                    try {
                        if (response.body() != null) {
                            Taste taste = new Taste(null, null, response.body().getOutfit());
                            mSwipeView.addView(new TinderCard(mContext, taste, mSwipeView, usr.getId()));
                        }

                    } catch (Exception e ) {
                        e.printStackTrace();
                        Log.e("e generateOutfits", e.toString());
                    }
                }
                @Override
                public void onFailure(Call<Outfit> call, Throwable t) {
                    Log.e("generateOutfits error", t.toString());
                }
            });
        }


    }
}
