package com.example.karl.karl.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.karl.karl.R;
import com.example.karl.karl.model.Taste;
import com.example.karl.karl.model.User;
import com.example.karl.karl.model.UserTaste;
import com.example.karl.karl.my_interface.GetUserDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.example.karl.karl.utils.ScreenUtils;
import com.google.gson.JsonElement;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

@Layout(R.layout.tinder_card_view)
public class TinderCard{

    @View(R.id.Clothe1ImageView)
    private ImageView clothe1ImageView;
    @View(R.id.Clothe2ImageView)
    private ImageView clothe2ImageView;
    @View(R.id.Clothe3ImageView)
    private ImageView clothe3ImageView;
    @View(R.id.Clothe4ImageView)
    private ImageView clothe4ImageView;

    private Taste moutfit;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private String BASE_URL = "http://18.184.156.66:8000/api/";
    private String mUsrId;
    private TinderCallback mTtinderCallback;

    public TinderCard(Context context, Taste outfit, SwipePlaceHolderView swipeView, String usrId) {
        mContext = context;
        moutfit = outfit;
        mSwipeView = swipeView;
        mUsrId = usrId;
        //Permet de ne pas avoir d'exception avec QuizStart1
        try{
            mTtinderCallback = (TinderCallback) context;
        }catch(ClassCastException e){
            mTtinderCallback = null;
        }

    }

    @Resolve
    private void onResolved(){
        switch (moutfit.getClothes().size()){
            case 0:
                break;
            case 1:
                //Load with Picasso
                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png")
                        .into(clothe1ImageView);
                break;
            case 2:
                //Load with Picasso
                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png")
                        .into(clothe1ImageView);

                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(1).getId() + ".png")
                        .into(clothe2ImageView);
                break;
            case 3:
                //Load with Picasso
                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png")
                        .into(clothe1ImageView);

                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(1).getId() + ".png")
                        .into(clothe2ImageView);

                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(2).getId() + ".png")
                        .into(clothe3ImageView);
                break;
            case 4:
                //Load with Picasso
                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png")
                        .into(clothe1ImageView);

                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(1).getId() + ".png")
                        .into(clothe2ImageView);

                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(2).getId() + ".png")
                        .into(clothe3ImageView);

                Picasso.with(mContext)
                        .load(BASE_URL + "uploads/" + moutfit.getClothes().get(3).getId() + ".png")
                        .into(clothe4ImageView);
                break;
        }
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        moutfit.setDecision(false);
        saveTaste();

        //mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        moutfit.setDecision(true);
        saveTaste();

    }

    private void saveTaste() {
        UserTaste userTaste = new UserTaste(mUsrId, moutfit);
        GetUserDataService service = RetrofitInstance.getRetrofitInstance().create(GetUserDataService.class);
        Call<JsonElement> call = service.updateTaste(userTaste);
        Log.e("url save", String.valueOf(call.request().url()));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Log.e("response save taste", response.body().toString());
                updateUI();
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(mContext, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        int tmp;
        if((mSwipeView.getAllResolvers().size() <= 1) && ( mContext instanceof QuizStart1)){
            Intent i = new Intent(mContext, Ootd.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.getApplicationContext().startActivity(i);
        }
        if((mSwipeView.getAllResolvers().size() <= 1) && ( mContext instanceof Ootd)){
            mTtinderCallback.newTinderCard();
        }
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    public interface TinderCallback{
        void newTinderCard();
    }
}