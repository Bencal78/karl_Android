package com.example.karl.karl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edouard on 20/09/2018.
 */

public class ootd extends AppCompatActivity {
    private static String UserId = null;
    private static String Id =null ;
    String BASE_URL = "http://18.184.156.66:8000/";
    String url;
    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.
    TextView tv3;
    TextView tv2;
    GridView gridView;

    public ootd() throws IOException {
    }

    ImageView iv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ootd);
        iv = findViewById(R.id.logo);
        tv3 = findViewById(R.id.textView2);
        //tv2 = findViewById(R.id.texttest2);
        gridView = (GridView) findViewById(R.id.gridview);
        List<String> images = new ArrayList<String>();
        List<Integer> values  = new ArrayList<Integer>();

        GridAdapter gridAdapter = new GridAdapter(this,values,images);
        gridView.setAdapter(gridAdapter);
        requestQueue = Volley.newRequestQueue(this);  // This setups up a new request queue which we will need to make HTTP requests.
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        assert acct != null;
        String GoogleId = String.valueOf(acct.getId());
        //Log.e("GoogleId ", GoogleId);
        GoogleIdToId(GoogleId);
        //Log.e("Id ", Id);


    }

    private void getOotd(String route,String id) {
        // First, we insert the username into the repo url.
        // The repo url is defined in GitHubs API docs (https://developer.github.com/v3/repos/).
        this.url =  route +id;
        Log.e("url ", url);
        final String [] images;

        // Next, we create a new JsonArrayRequest. This will use Volley to make a HTTP request
        // that expects a JSON Array Response.
        // To fully understand this, I'd recommend readng the office docs: https://developer.android.com/training/volley/index.html
        images = new String[0];
        final JsonObjectRequest arrReq = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Check the length of our response (to see if the user has any repos)

                        try {
                            /// GET ALL CLOTHES
                            int i=0;
                            while (response.getJSONArray("outfit").getJSONObject(0).optString("_id")!=null) {
                                String text = response.getJSONArray("outfit").getJSONObject(i).optString("_id");

                                i++;
                            }
                            //tv2.setText(a);
                            //tv3.setText(text2);

                            //String image_url = BASE_URL + "api/uploads/" + text + ".png";
                            //new DownLoadImageTask(iv).execute(image_url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //String image_url = BASE_URL + "api/uploads/" + clothe1_id + ".png";
                        //new DownLoadImageTask(iv).execute(image_url);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue.add(arrReq);

    }
    private void GoogleIdToId(String GoogleId){

        String url = "http://18.184.156.66:8000/api/users?idGoogle=";
        url = url + GoogleId;
        Log.e("url", url);

        final String OotdUrl = "http://18.184.156.66:8000/api/pyreq?func_name=return_outfit&id=";
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Check the length of our response (to see if the user has any repos)
                        try {
                            /// GOOGLE ID TO ID
                            UserId = response.getJSONObject(0).getString("_id");
                            //tv.setText("response :" + UserId);

                            getOotd(OotdUrl,UserId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        Log.e("Volley", error.toString());
                    }
                }
        );

        requestQueue.add(arrReq);




    }
}

