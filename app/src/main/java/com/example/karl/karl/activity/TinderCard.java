package com.example.karl.karl.activity;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.karl.karl.R;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Taste;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.ArrayList;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

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

    public TinderCard(Context context, Taste outfit, SwipePlaceHolderView swipeView) {
        mContext = context;
        moutfit = outfit;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        switch (moutfit.getClothes().size()){
            case 0:
                break;
            case 1:
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png").into(clothe1ImageView);
                break;
            case 2:
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png").into(clothe1ImageView);
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(1).getId() + ".png").into(clothe2ImageView);
                break;
            case 3:
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png").into(clothe1ImageView);
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(1).getId() + ".png").into(clothe2ImageView);
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(2).getId() + ".png").into(clothe3ImageView);
                break;
            case 4:
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(0).getId() + ".png").into(clothe1ImageView);
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(1).getId() + ".png").into(clothe2ImageView);
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(2).getId() + ".png").into(clothe3ImageView);
                Glide.with(mContext).load(BASE_URL + "uploads/" + moutfit.getClothes().get(3).getId() + ".png").into(clothe4ImageView);
                break;
        }
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        moutfit.setDecision(false);
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
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}