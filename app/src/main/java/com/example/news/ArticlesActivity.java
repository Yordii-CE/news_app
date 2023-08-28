package com.example.news;
import com.example.news.helpers.Alert;
import com.example.news.http.response.ArticlesResponse;
import com.example.news.http.retrofit.ArticlesService;
import com.example.news.http.retrofit.RetrofitHelper;
import com.example.news.list.ArticleAdapter;
import com.example.news.models.ArticleModel;
import com.example.news.services.NewsService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
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
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesActivity extends AppCompatActivity implements IBottomSheetListener {
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;

    private TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbarTitle);


        ImageView overflowIcon = findViewById(R.id.action_overflow);
        overflowIcon.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, overflowIcon);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.dropdown_menu, popupMenu.getMenu());


            popupMenu.setOnMenuItemClickListener(item -> {
                String tittle = item.getTitle().toString();
                if(tittle.equals("Buscar")){
                    CustomBottomSheetDialog dialog = new CustomBottomSheetDialog();
                    dialog.listener = this;
                    dialog.show(getSupportFragmentManager(), "CustomBottomSheetDialog");
                }else{
                    fetchNewsData(tittle);
                    toolbarTitle.setText("Noticias " + tittle);
                }

                return true;
            });

            popupMenu.show();
        });



        ImageView actionFavorite = findViewById(R.id.action_favorite);
        actionFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticlesActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
        // Mostrar el loader
        CircularProgressIndicator loaderCircularProgress = findViewById(R.id.loaderCircularProgress);
        loaderCircularProgress.setVisibility(View.VISIBLE);

        // Ocultar el contenido principal mientras se carga
        findViewById(R.id.newsContainer).setVisibility(View.GONE);

        fetchNewsData("tesla");
        toolbarTitle.setText("Noticias Tesla");
    }

    private void fetchNewsData(String q) {
        NewsService newsService = new NewsService();
        newsService.getNewsData(q, new NewsService.DataCallback() {
            @Override
            public void onDataReceived(List<ArticleModel> articlesList) {
                // Ocultar el loader
                ProgressBar loaderCircularProgress = findViewById(R.id.loaderCircularProgress);
                loaderCircularProgress.setVisibility(View.GONE);
                // Mostrar el contenido principal
                findViewById(R.id.newsContainer).setVisibility(View.VISIBLE);


                Alert.message(findViewById(R.id.newsContainer), "TOTAL DE ARTICULOS OBTENIDOS: " + articlesList.size(), Color.GRAY);

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(ArticlesActivity.this));

                articleAdapter = new ArticleAdapter(ArticlesActivity.this, articlesList);
                recyclerView.setAdapter(articleAdapter);
            }

            @Override
            public void onDataError(String errorMessage) {
                // Manejar el error de la solicitud
                Alert.message(findViewById(R.id.newsContainer), errorMessage, Color.RED);
            }
        });
    }

    @Override
    public void onSearchButtonClicked(String keyword) {
        fetchNewsData(keyword);
        toolbarTitle.setText("Noticias " + keyword);
    }

    @Override
    public void onCloseButtonClicked() {
        // Hacer algo en respuesta al clic en el botón de cerrar
    }

}