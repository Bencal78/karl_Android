package com.example.karl.karl.my_interface;

import com.example.karl.karl.model.Outfit;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPyreqDataService {
    @GET("pyreq/return_outfit")
    Call<Outfit> getOutfit(@Query("id") String id);

    @GET("pyreq/return_outfit_rl")
    Call<Outfit> getOutfitRl(@Query("id") String id);

    @GET("pyreq/return_weather")
    Call<JsonElement> getWeather(@Query("lat") String lat, @Query("long") String longitude);
}
