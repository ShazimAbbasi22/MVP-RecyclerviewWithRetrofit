package com.example.mvp_retrofitwithrecyclerview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import com.example.mvp_retrofitwithrecyclerview.R;
import com.example.mvp_retrofitwithrecyclerview.databinding.ItemLayoutBinding;
import com.example.mvp_retrofitwithrecyclerview.model.Article;
import com.example.mvp_retrofitwithrecyclerview.model.News;
import com.example.mvp_retrofitwithrecyclerview.model.Post;
import com.example.mvp_retrofitwithrecyclerview.model.QuantityListener;
import com.example.mvp_retrofitwithrecyclerview.view.DetailActivity;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    //    private final List<News> news;
    private final List<Article> articles;
    private final OnPostActionListener listener;  // Listener for handling delete and edit actions
    Context context;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private final Set<Integer> selectedPositions = new HashSet<>();  // To track selected item positions
    public boolean isSelectionMode = false; // To track if we're in selection mode
    private final Set<Article> selectedNews = new HashSet<>(); // To track selected posts

    public PostAdapter(List<Article> articles, OnPostActionListener listener, Context context, QuantityListener quantityListener) {
        this.articles = articles;
        this.listener = listener;
        this.context = context;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout using ViewBinding
        ItemLayoutBinding binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Article articleResponse = articles.get(holder.getAdapterPosition());

        holder.binding.titleView.setText(articleResponse.getTitle());
        holder.binding.bodyView.setText(articleResponse.getDescription());

        holder.binding.editButton.setOnClickListener(v -> listener.updateItem(articleResponse, position));
        holder.binding.deleteButton.setOnClickListener(v -> listener.onDeleteItem(articleResponse, position));

        Glide.with(context).load(articleResponse.getUrlToImage()).placeholder(R.drawable.img_1).error(R.drawable.img).circleCrop().into(holder.binding.imageView);

        // Save/restore the open/close state.
        // You need to provide a String id which uniquely defines the data object.
        viewBinderHelper.bind(holder.binding.swipeLayout, articleResponse.getUrl());

        // Reset selection state
        holder.binding.cbSelect.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.binding.cbSelect.setChecked(selectedNews.contains(articleResponse));

        // Handle item click to start DetailActivity
        holder.binding.root.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("title", articleResponse.getTitle());  // Pass title
            intent.putExtra("body", articleResponse.getContent());  // Pass body
            intent.putExtra("image", articleResponse.getUrlToImage());  // Pass image
            context.startActivity(intent);  // Start new activity
        });

        holder.binding.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Handle long click to enter selection mode
                if (!articles.isEmpty()) {
                    isSelectionMode = true;
                    selectedPositions.add(position);
                    selectedNews.add(articleResponse);
//                    holder.binding.cbSelect.setVisibility(View.VISIBLE);
//                    holder.binding.cbSelect.setChecked(true);
                    notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemLayoutBinding binding;

        public MyViewHolder(ItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    // Interface for handling item actions (edit, delete)
    public interface OnPostActionListener {
        void updateItem(Article article, int position);  // Handle edit action

        void onDeleteItem(Article article, int position);  // Handle delete action
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return articles.size();
    }

    public void removeSelectedItems() {
        // Convert selected positions to a list and sort in reverse order
        List<Integer> positionsToRemove = new ArrayList<>(selectedPositions);
        positionsToRemove.sort(Collections.reverseOrder());  // Sort in reverse order to prevent index shifting

        // Remove items at selected positions
        for (int position : positionsToRemove) {
            articles.remove(position);
            notifyItemRemoved(position);
        }

        // Clear the selection sets after removing
        selectedPositions.clear();
        selectedNews.clear();

        notifyDataSetChanged();  // Notify adapter of data change
        isSelectionMode = false;  // Exit selection mode
    }

    public void clearSelection() {
        selectedNews.clear();
        isSelectionMode = false; // Exit selection mode
        notifyDataSetChanged(); // Refresh UI
    }
}
