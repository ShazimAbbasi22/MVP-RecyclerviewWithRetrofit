package com.example.mvp_retrofitwithrecyclerview.view;

import android.view.MenuItem;

import com.example.mvp_retrofitwithrecyclerview.model.News;
import com.example.mvp_retrofitwithrecyclerview.model.Post;

import java.util.List;

public interface MainView {

    void showProgressbar();
    void hideProgressbar();
    void showData(News news);
    void showError(String message);

}
