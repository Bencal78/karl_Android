package com.example.karl.karl.my_interface;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPyreqDataService {
    @GET("pyreq")
    Call<JSONObject> getPyreq(@Query("func_name") String func_name, @Query("id") String id);
}
