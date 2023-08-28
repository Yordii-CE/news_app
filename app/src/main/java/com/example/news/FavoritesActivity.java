package com.example.news;

import com.example.news.database.DatabaseHelper;
import com.example.news.helpers.Alert;
import com.example.news.list.ArticleAdapter;
import com.example.news.models.ArticleModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);


        databaseHelper = new DatabaseHelper(this);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Favoritos");

        ImageView action_overflow = findViewById(R.id.action_overflow);
        action_overflow.setVisibility(View.GONE);

        ImageView actionNews = findViewById(R.id.action_news);
        actionNews.setVisibility(View.VISIBLE);
        actionNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, ArticlesActivity.class);
                startActivity(intent);
            }
        });

        ImageView actionFavorite = findViewById(R.id.action_favorite);
        actionFavorite.setVisibility(View.GONE);
        actionFavorite.getLayoutParams().width = 0;
        actionFavorite.getLayoutParams().height = 0;


        List<ArticleModel> articlesResponse = databaseHelper.getAllFavorites();

        Alert.message(findViewById(R.id.newsContainer), "TOTAL DE ARTICULOS FAVORITOS: " + articlesResponse.size(),  Color.GRAY);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavoritesActivity.this));

        articleAdapter = new ArticleAdapter(FavoritesActivity.this, articlesResponse);
        recyclerView.setAdapter(articleAdapter);

    }

}