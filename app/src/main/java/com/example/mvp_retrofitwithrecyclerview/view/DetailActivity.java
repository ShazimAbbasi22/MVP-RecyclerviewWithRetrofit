package com.example.mvp_retrofitwithrecyclerview.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.mvp_retrofitwithrecyclerview.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the data passed via intent
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        String image = getIntent().getStringExtra("image");

        // Set the title and body to the TextViews in the layout
        TextView tvTitle = findViewById(R.id.titleDetail);
        TextView tvBody = findViewById(R.id.bodyDetail);
        ImageView imageView = findViewById(R.id.imageView2);

        if (title != null) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);

        }

        if (body != null) {
            tvBody.setText(body);
        } else {
            tvBody.setVisibility(View.GONE);
        }

        if (image != null) {
            Glide.with(this).load(image).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }

    }

}