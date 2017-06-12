package com.example.beatriz.listatcc;

import com.example.beatriz.listatcc.Model.GoogleAPIObjectResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Beatriz on 17/09/2016.
 */
public interface GoogleApiRequest {
    String GOOGLE_API_KEY = "AIzaSyBHDL_nsuwPesxmxetBmJkfRlxJLd-Y43k";

    @GET("json")
    Call<GoogleAPIObjectResponse> getSupermarket(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("rankby") String rankby,
            @Query("types") String types,
            @Query("key") String key);
}
