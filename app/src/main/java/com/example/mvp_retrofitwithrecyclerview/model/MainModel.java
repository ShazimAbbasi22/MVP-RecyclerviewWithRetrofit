package com.example.mvp_retrofitwithrecyclerview.model;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainModel {

    String BASE_URL = "https://newsapi.org/";
    String API_KEY = "08bc0948dff449eea219cc018f958ada";
    ApiInterface apiInterface;

    public MainModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }


    public void getData(Callback<News> callback) {
        Call<News> call = apiInterface.getNews("us","business", API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                    Log.d("apiResponse", "Data fetched successfully");
                } else {
                    callback.onFailure(call, new Throwable("Error while fetching data from Api"));
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }
}
