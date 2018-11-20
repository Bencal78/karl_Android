package com.example.karl.karl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class QuizStart extends AppCompatActivity {
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
        tv = findViewById(R.id.sample_text);
        iv = findViewById(R.id.sample_image);

        final CallBack callback = new CallBack() {
            @Override
            public void onProgress() {

            }

            @Override
            public void onResult(String result) {

            }

            @Override
            public void onCancel() {

            }
        };
        String url= "http://18.184.156.66:8000/api/clothes?_id=5a37db5af128eb034baa558c";
        //JSONObject postData = new JSONObject();
        //try {
        //    postData.put("bodyparts",1);
        //} catch (JSONException e) {
        //    e.printStackTrace();
        //}
        //HTTPAsyncTask asyncTaskPOST = new HTTPAsyncTask(mContext,callback, null, postData, "POST");
       // asyncTaskPOST.execute(url);
        HTTPAsyncTask asyncTaskGET = new HTTPAsyncTask(mContext,callback, null, null, "GET");
        AsyncTask<String, Void, String> result = asyncTaskGET.execute(url);
        try {
            tv.setText(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
