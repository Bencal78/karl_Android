package com.example.karl.karl.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
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
    String BASE_URL = "http://18.184.156.66:8000/";
    String url;
    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
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
                    String image_url = BASE_URL + "api/uploads/" + clothe1_id + ".png";
                    modelList.add(new Model("", image_url));

                }

                CardAdapter cardAdapter = getCardAdapter();
                swipeCardsView.setAdapter(cardAdapter);
                //generateClotheList(response.body().getClotheArrayList());
            }

            @Override
            public void onFailure(Call<ArrayList<Clothe>> call, Throwable t) {
                Log.e("Something went wrong", t.getMessage());
                Toast.makeText(QuizStart.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        /*
        String url= "api/clothes";
        requestQueue = Volley.newRequestQueue(this);  // This setups up a new request queue which we will need to make HTTP requests.
        */
        //getClothes(url);
    }

    private CardAdapter getCardAdapter() {
        CardAdapter cardAdapter = new CardAdapter(modelList, this);
        return cardAdapter;
    }

    /*
    private void getClothes(String route) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        Intent myIntent = new Intent(QuizStart.this, Ootd.class);
        startActivity(myIntent);
        this.url = this.BASE_URL + route;
        Log.e("url ", url);


       // final List<Model> modelList = new ArrayList<>();

        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
        // that expects a JSON Array Response.
        // To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Check the length of our response (to see if the user has any repos)
                        try {
                            // recupere les 10 id qu tu souhaites afficher
                            //add dynamiquement a Modellist les url avec l'id recupéré
                            for(int id = 0; id < 10; id++){
                                String clothe1_id= response.getJSONObject(id).getString("_id");
                                String image_url = BASE_URL + "api/uploads/" + clothe1_id + ".png";
                                modelList.add(new Model("", image_url));

                            }

                            CardAdapter cardAdapter = getCardAdapter();
                            swipeCardsView.setAdapter(cardAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        tv.setText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );

        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
    }
    */
}
