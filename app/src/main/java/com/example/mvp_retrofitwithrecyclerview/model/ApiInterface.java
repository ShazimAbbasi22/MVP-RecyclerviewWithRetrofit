package com.example.mvp_retrofitwithrecyclerview.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("/posts")
//    Call<List<Post>> getPosts();

    @GET("/v2/everything")
    Call<News> getNews(
//            @Query("country") String country,
            @Query("q") String q,
            @Query("apiKey") String apiKey
    );

}
