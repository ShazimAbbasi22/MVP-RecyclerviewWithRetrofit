package com.example.mvp_retrofitwithrecyclerview.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvp_retrofitwithrecyclerview.R;
import com.example.mvp_retrofitwithrecyclerview.adapter.PostAdapter;
import com.example.mvp_retrofitwithrecyclerview.model.Article;
import com.example.mvp_retrofitwithrecyclerview.model.News;
import com.example.mvp_retrofitwithrecyclerview.model.Post;
import com.example.mvp_retrofitwithrecyclerview.model.QuantityListener;
import com.example.mvp_retrofitwithrecyclerview.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView, PostAdapter.OnPostActionListener, QuantityListener {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    MainPresenter presenter;
    PostAdapter adapter;
    List<Article> articles;
    List<News> news = new ArrayList<>();
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView and ProgressBar.
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);

        // Initialize your article list
        articles = new ArrayList<>();  // Initialize it as an empty list

        // Set up the RecyclerView with the adapter.
        adapter = new PostAdapter(articles, this, this, this);  // Pass this as listener for delete and edit
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the Presenter and fetch data.
        presenter = new MainPresenter(this);
        presenter.getDataFromAPI();

        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("es-ES");
        // Call this on the main thread as it may require Activity.restart()
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and add items
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (adapter.isSelectionMode) {
            // If in selection mode, clear selection
            adapter.clearSelection();
        } else {
            // Otherwise, perform normal back button action
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks
        if (item.getItemId() == R.id.action_delete_selected) {
            confirmDeleteSelectedItems();
            // Show confirmation dialog for deletion
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Confirm before deleting selected items
    private void confirmDeleteSelectedItems() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Selected")
                .setMessage("Are you sure you want to delete selected items?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    adapter.removeSelectedItems();  // Delete selected items
                    Toast.makeText(MainActivity.this, "Selected items deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(News data) {
        articles.clear();
        articles.addAll(data.getArticles());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Handling edit action
    @Override
    public void updateItem(Article article, int position) {

        // Show a dialog to edit the post
        showEditDialog(article, position);
    }

    @Override
    public void onDeleteItem(Article article, int position) {
        // Remove the Post from the list and notify the adapter.
        articles.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    // Method to show a dialog box for editing a Post.
    private void showEditDialog(@NonNull Article article, int position) {

        // Create an AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Post");

        // Inflate a custom dialog layout.
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_post, null);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etBody = view.findViewById(R.id.etBody);

        // Prepopulate the dialog with the current title and body.
        etTitle.setText(article.getTitle());
        etBody.setText(article.getDescription());
        builder.setView(view);

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Update the Post with the new values.
            article.getTitle();
            article.getDescription();
            adapter.notifyItemChanged(position);  // Notify the adapter of the change.
            Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onQuantityChanged(List<Post> posts) {
        Toast.makeText(this, posts.toString(), Toast.LENGTH_SHORT).show();
    }


}