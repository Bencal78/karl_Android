package com.example.karl.karl.my_interface;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPyreqDataService {
    @GET("pyreq")
    Call<JsonElement> getPyreq(@Query("func_name") String func_name, @Query("id") String id);
}
