package com.example.karl.karl.my_interface;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetGoogleDataService {
    @GET("plus/v1/people/{id}")
    Call<JsonElement> getPeople(@Path("id") String id, @Query("personFields") String personFields, @Query("key") String google_api_key);
}
