package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.database.DatabaseHelper;
import com.example.news.helpers.Alert;
import com.example.news.models.ArticleModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class ArticleActivity extends AppCompatActivity {
    private boolean isHeartFilled;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        databaseHelper = new DatabaseHelper(this);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Articulo");

        ImageView actionFavorite = findViewById(R.id.action_favorite);
        actionFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        ImageView action_overflow = findViewById(R.id.action_overflow);
        action_overflow.setVisibility(View.GONE);

        //Intent data
        String author = getIntent().getStringExtra("author");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String descriptionPlusMore = description + "Ver más";

        String url = getIntent().getStringExtra("url");
        String urlToImage = getIntent().getStringExtra("urlToImage");
        String publishedAt = getIntent().getStringExtra("publishedAt");
        String content = getIntent().getStringExtra("content");


        TextView authorText = findViewById(R.id.authorText);
        TextView titleText = findViewById(R.id.articleTitle);
        TextView articleDescription = findViewById(R.id.articleDescription);
        TextView publishedAtText = findViewById(R.id.publishedAtText);
        ImageView articleImage = findViewById(R.id.articleImage);

        authorText.setText(author);
        titleText.setText(title);
        articleDescription.setText(description);
        publishedAtText.setText(publishedAt.split("T")[0]);
        Picasso.get().load(urlToImage).into(articleImage);

        // Crear el ClickableSpan para el enlace "ver más"
        ClickableSpan clickableSpan = new ClickableSpan() {
            private boolean isClicked = false;
            @Override
            public void onClick(View widget) {
                // Acción cuando se hace clic en el enlace
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                // Verifica si hay una aplicación que pueda manejar la acción
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        };

        // Construir el texto con el enlace
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(descriptionPlusMore);
        int startIndex = descriptionPlusMore.indexOf("Ver más");
        spannableStringBuilder.setSpan(clickableSpan, startIndex, startIndex + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Establecer el texto en el TextView
        articleDescription.setText(spannableStringBuilder);
        articleDescription.setMovementMethod(LinkMovementMethod.getInstance());


        //Heart
        ImageView heartIcon = findViewById(R.id.heartIcon);
        ArticleModel article = databaseHelper.getFavoriteByTitle(title);
        if(article == null){
            //Aun no esta en favoritos
            heartIcon.setImageResource(R.drawable.heart_white);
            isHeartFilled = false;
        }else{
            //Ya esta en esta en favoritos
            heartIcon.setImageResource(R.drawable.heart_red);
            isHeartFilled = true;
        }
        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHeartFilled = !isHeartFilled; // Cambia el estado del corazón al hacer clic
                if (isHeartFilled) {
                    heartIcon.setImageResource(R.drawable.heart_red);
                    // Insertar favorito
                    long newFavorite = databaseHelper.insertFavorite(author, title, description, url, urlToImage, publishedAt, content);
                    if (newFavorite != -1) {
                        Alert.message(findViewById(R.id.appContainer), "Articulo agregado a favoritos.", Color.GREEN);
                    } else {
                        Alert.message(findViewById(R.id.appContainer), "Error al agregar.", Color.WHITE);
                    }
                } else {
                    heartIcon.setImageResource(R.drawable.heart_white);
                    //Eliminar favorito
                    int rowsDeleted = databaseHelper.deleteFavoriteByTitle(title);
                    if (rowsDeleted > 0) {
                        Alert.message(findViewById(R.id.appContainer), "Articulo eliminado de favoritos.", Color.RED);
                    } else {
                        Alert.message(findViewById(R.id.appContainer), "Error al eliminar.", Color.WHITE);
                    }
                }

            }
        });
    }

}