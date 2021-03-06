package com.example.karl.karl.my_interface;
import com.example.karl.karl.model.Clothe;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetClotheDataService {
    @GET("clothes")
    Call<ArrayList<Clothe>> getClothe();

    @GET("uploads/{id}.png")
    Call<String> getImageClothe(@Path("id") String id);

    /**
     * URL MANIPULATION
     * @since Not used, Just to know how to use @query to get JSONObject
     * */
    @GET("clothes")
    Call<ArrayList<Clothe>> getClotheById(@Query("_id") String id);


    /**
     * URL MANIPULATION
     * HTTP request body with the @Body annotation
     */
    @POST("clothes/")
    Call<Clothe> createClothe(@Body RequestBody body);


    /**
     * FORM ENCODED AND MULTIPART
     * Multipart requests are used when @Multipart is present on the method.
     * Parts are declared using the @Part annotation.
     * */
    @PUT("clothes")
    Call<Clothe> updateClothe (@Body RequestBody body);

    @DELETE("clothes")
    Call<Clothe> deleteClothe(@Body RequestBody body);
}
