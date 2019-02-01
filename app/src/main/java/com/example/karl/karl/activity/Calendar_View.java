package com.example.karl.karl.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.karl.karl.R;
import com.example.karl.karl.my_interface.GetGoogleDataService;
import com.example.karl.karl.network.GoogleRetrofitInstance;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class Calendar_View extends AppCompatActivity {

    private ListView listView;
    private android.support.v7.widget.Toolbar toolbar;
    private ArrayList<String> PreferredDay = new ArrayList<>();
    private ArrayList<String> PreferredTime = new ArrayList<>();

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.day_detail);
        setupUIViews();
        initToolbar();
        getCalendar();
    }

    private void setupUIViews(){
        listView = findViewById(R.id.lvDayDetail);
        toolbar = findViewById(R.id.ToolbarDayDetail);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView(ArrayList<JSONObject> obj){
        for(int i=0; i<obj.size(); i++){
            try {
                PreferredDay.add((String) obj.get(i).get("subject"));
                PreferredTime.add(obj.get(i).get("start_hour") + "-" + obj.get(i).get("end_hour"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(Calendar_View.this, PreferredDay, PreferredTime);
        listView.setAdapter(simpleAdapter);

    }

    private void getCalendar() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        GetGoogleDataService service = GoogleRetrofitInstance.getRetrofitInstance().create(GetGoogleDataService.class);
        Call<JsonElement> call = service.getCalendar(acct.getEmail(), getString(R.string.calendar_key));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //Log.e("response", response.body().toString());
                try {
                    ArrayList<JSONObject> res = new ArrayList<>();
                    if(response.body() != null){
                        JSONObject obj = new JSONObject(response.body().toString());
                        if(obj.has("items")){
                            JSONArray items = obj.getJSONArray("items");
                            Date now = new Date();
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //If you need time just put specific format for time like 'HH:mm:ss'
                            String nowStr = formatter.format(now);
                            String nowStrDay = nowStr.substring(0, 10);
                            String nowStringHour = nowStr.substring(11);

                            for(int i=0; i<items.length(); i++){
                                JSONObject item = (JSONObject) items.get(i);
                                if(item.has("start") && item.has("end")){
                                    JSONObject start = (JSONObject) item.get("start");
                                    JSONObject end = (JSONObject) item.get("end");
                                    if(start.has("dateTime") && end.has("dateTime")){
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                        String startDateTime = (String) start.get("dateTime");
                                        Date startDate = dateFormat.parse(startDateTime);

                                        String endDateTime = (String) end.get("dateTime");
                                        Date endDate = dateFormat.parse(endDateTime);

                                        String startDateStr = formatter.format(startDate);
                                        String startDateStrDay = startDateStr.substring(0, 10);
                                        String startDateStringHour = startDateStr.substring(11);

                                        String endDateStr = formatter.format(endDate);
                                        String endDateStrDay = endDateStr.substring(0, 10);
                                        String endDateStringHour = endDateStr.substring(11);
                                        if(startDateStrDay.equals(nowStrDay)){
                                            //Log.e("ok ", "it worked very well");
                                            JSONObject res_item = new JSONObject();
                                            res_item.put("start_hour", startDateStringHour);
                                            res_item.put("end_hour", endDateStringHour);
                                            res_item.put("subject", item.get("summary"));
                                            res.add(res_item);
                                        }
                                    }
                                    else if(start.has("date") && end.has("date")){
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        String startDateTime = (String) start.get("date");
                                        Date startDate = dateFormat.parse(startDateTime);

                                        String endDateTime = (String) end.get("date");
                                        Date endDate = dateFormat.parse(endDateTime);

                                        String startDateStr = formatter.format(startDate);
                                        String startDateStrDay = startDateStr.substring(0, 10);

                                        String endDateStr = formatter.format(endDate);
                                        String endDateStrDay = endDateStr.substring(0, 10);
                                        if(startDateStrDay.equals(nowStrDay)){
                                            JSONObject res_item = new JSONObject();
                                            res_item.put("start_hour", "00:00:00");
                                            res_item.put("end_hour", "23:59:59");
                                            res_item.put("subject", item.get("summary"));
                                            res.add(res_item);
                                        }
                                    }
                                    else {
                                        Log.e("start not have", start.toString());
                                    }
                                }
                                else{
                                    Log.e("item not have", item.toString());
                                }
                            }
                            setupListView(res);
                        }
                    }
                    else{
                        Log.e("error", "response is null");
                    }
                    if(res.size() == 0){
                        JSONObject res_item = new JSONObject();
                        res_item.put("start_hour", "00:00:00");
                        res_item.put("end_hour", "23:59:59");
                        res_item.put("subject", "No events found for today");
                        res.add(res_item);
                        setupListView(res);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("error in getCalendar", t.getMessage());
            }
        });
    }

    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView subject, time;
        private ArrayList<String> subjectArray;
        private ArrayList<String> timeArray;
        private LetterImageView letterImageView;

        public SimpleAdapter(Context context, ArrayList<String> subjectArray, ArrayList<String> timeArray){
            mContext = context;
            this.subjectArray = subjectArray;
            this.timeArray = timeArray;
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return subjectArray.size();
        }

        @Override
        public Object getItem(int position) {
            return subjectArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = layoutInflater.inflate(R.layout.daydetailsingleitem, null);
            }

            subject = (TextView)convertView.findViewById(R.id.tvSubjectDayDetails);
            time = (TextView)convertView.findViewById(R.id.tvTimeDayDetail);
            letterImageView = (LetterImageView)convertView.findViewById(R.id.ivDayDetails);

            subject.setText(subjectArray.get(position));
            time.setText(timeArray.get(position));

            letterImageView.setOval(true);
            letterImageView.setLetter(subjectArray.get(position).charAt(0));

            return convertView;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
