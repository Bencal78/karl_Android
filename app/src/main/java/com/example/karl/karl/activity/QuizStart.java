package com.example.karl.karl.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karl.karl.R;
import com.example.karl.karl.adapter.CardAdapter;
import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.Model;
import com.example.karl.karl.my_interface.GetClotheDataService;
import com.example.karl.karl.network.RetrofitInstance;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class QuizStart extends AppCompatActivity{
    Button button;
    private SwipeCardsView swipeCardsView;
    List<Model> modelList = new ArrayList<>();

    public QuizStart() throws IOException {
    }

    ImageView iv;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);
        Context mContext = getApplicationContext();
        // Example of a call to a native method

        swipeCardsView = (SwipeCardsView)findViewById(R.id.swipeCardsView);
        swipeCardsView.retainLastCard(false);
        swipeCardsView.enableSwipe(true);

        /** Create handle for the RetrofitInstance interface*/
        GetClotheDataService service = RetrofitInstance.getRetrofitInstance().create(GetClotheDataService.class);
        Call<ArrayList<Clothe>> call = service.getClothe();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<Clothe>>() {
            @Override
            public void onResponse(Call<ArrayList<Clothe>> call, retrofit2.Response<ArrayList<Clothe>> response) {
                Log.e("size", String.valueOf(response.body().size()));
                Log.e("response", response.body().get(0).getName());
                // recupere les 10 id qu tu souhaites afficher
                //add dynamiquement a Modellist les url avec l'id recupéré
                for(int id = 0; id < 10; id++){
                    String clothe1_id= response.body().get(id).getId();
                    String image_url = getString(R.string.base_url) + "uploads/" + clothe1_id + ".png";
                    modelList.add(new Model("", image_url));

                }

                CardAdapter cardAdapter = getCardAdapter();
                swipeCardsView.setAdapter(cardAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Clothe>> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(QuizStart.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CardAdapter getCardAdapter() {
        CardAdapter cardAdapter = new CardAdapter(modelList, this);
        return cardAdapter;
    }
}
