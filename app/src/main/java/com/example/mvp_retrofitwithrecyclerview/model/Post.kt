package com.example.mvp_retrofitwithrecyclerview.model

import androidx.room.PrimaryKey

data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val title: String,
    val body: String
)
