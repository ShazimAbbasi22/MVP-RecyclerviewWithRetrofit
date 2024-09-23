package com.example.mvp_retrofitwithrecyclerview.presenter;
import com.example.mvp_retrofitwithrecyclerview.model.Article;
import com.example.mvp_retrofitwithrecyclerview.model.MainModel;
import com.example.mvp_retrofitwithrecyclerview.model.News;
import com.example.mvp_retrofitwithrecyclerview.model.Post;
import com.example.mvp_retrofitwithrecyclerview.view.MainView;

import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {

    MainView view;
    MainModel model;
    News news;
    Article articles;
    public MainPresenter(MainView view) {
        this.view = view;
        this.model = new MainModel();
    }

    public void getDataFromAPI() {

        view.showProgressbar();

        model.getData(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                view.hideProgressbar();
                news = response.body();
                view.showData(news);
            }

            @Override
            public void onFailure(Call<News> call, Throwable throwable) {

                view.hideProgressbar();
                view.showError(throwable.getMessage());
            }
        });
    }


//    // Update the item after editing
//    public void updateItem(Article updatedItem, int position) {
//        Article newsResponse = news.get(position);
////        News newsResponse = news.get(position);
//        newsResponse.getTitle();  // Update the item in the local list
//
//        view.showData(news);  // Refresh the RecyclerView with updated data
//    }
//
//    // Handle deletion of an item
//    public void onDeleteItem(Article onDeleteItem,int position) {
//        news.remove(position);  // Remove item from the local list
//        view.showData(news);  // Update the RecyclerView
//    }
}
