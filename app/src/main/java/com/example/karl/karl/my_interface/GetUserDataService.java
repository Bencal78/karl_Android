package com.example.karl.karl.my_interface;

import com.example.karl.karl.model.Clothe;
import com.example.karl.karl.model.User;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface GetUserDataService {
    @GET("users")
    Call<ArrayList<User>> getUser();

    /**
     * URL MANIPULATION
     * @since Not used, Just to know how to use @query to get JSONObject
     * */
    @GET("users")
    Call<ArrayList<User>> getUserById(@Query("_id") String id);


    /**
     * URL MANIPULATION
     * HTTP request body with the @Body annotation
     */
    @POST("users")
    Call<Clothe> createUser(@Body RequestBody body);


    /**
     * FORM ENCODED AND MULTIPART
     * Multipart requests are used when @Multipart is present on the method.
     * Parts are declared using the @Part annotation.
     * */
    @PUT("users")
    Call<User> updateUser (@Body RequestBody body);

    @PUT("users/addTaste")
    Call<User> updateTaste(@Body RequestBody body);

    @PUT("users/addClothe")
    Call<User> updateClothe(@Body RequestBody body);

    @DELETE("users")
    Call<User> deleteUser(@Body RequestBody body);
}
