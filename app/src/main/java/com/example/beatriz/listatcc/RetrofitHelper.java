package com.example.beatriz.listatcc;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Beatriz on 17/09/2016.
 */
public class RetrofitHelper<T> {
    Retrofit retrofit;
    OkHttpClient okHttpClient;
    T t;
    String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/";

    public interface RetrofitHelperCallback {
        void onResponse(Call<Object> call, Response<Object> response);

        void onFailure(Call<Object> call, Throwable t);
    }

    RetrofitHelperCallback retrofitHelperCallback;

    public RetrofitHelper(Context context, RetrofitHelperCallback retrofitHelperCallback, Class<T> service) {
        this.retrofitHelperCallback = retrofitHelperCallback;

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        t = retrofit.create(service);
    }

    public T getT() {
        return t;
    }

    public void execute(Call<Object> call) {
        call.clone().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                retrofitHelperCallback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                retrofitHelperCallback.onFailure(call, t);
            }
        });
    }
}
