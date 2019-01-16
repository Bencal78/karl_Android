package com.example.karl.karl.my_interface;
import com.example.karl.karl.model.Clothe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GetClotheDataService {
    @GET("clothes")
    Call<ArrayList<Clothe>> getClothe();

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
    Call<Clothe> createClothe(@Body Clothe clothe);


    /**
     * FORM ENCODED AND MULTIPART
     * Multipart requests are used when @Multipart is present on the method.
     * Parts are declared using the @Part annotation.
     * */
    @Multipart
    @PUT("clothes")
    Call<Clothe> updateClothe (@Part("photo") RequestBody photo, @Part("description") RequestBody description);
}
