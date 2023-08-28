package com.example.news.services;

import com.example.news.http.response.ArticlesResponse;
import com.example.news.http.retrofit.ArticlesService;
import com.example.news.http.retrofit.RetrofitHelper;
import com.example.news.models.ArticleModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsService {
    public void getNewsData(String query, final NewsService.DataCallback callback) {

        ArticlesService apiService = RetrofitHelper.getInstance().create(ArticlesService.class);
        Call<ArticlesResponse> call = apiService.getNews(query);

        call.enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                if (response.isSuccessful()) {
                    ArticlesResponse articlesResponse = response.body();
                    callback.onDataReceived(articlesResponse.getArticlesModelList());
                } else {
                    callback.onDataError("ERROR AL CONSUMIR DATOS DE NEWSAPI");
                }
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                callback.onDataError("ERROR EN LA SOLICITUD");
            }
        });
    }

    public interface DataCallback {
        void onDataReceived(List<ArticleModel> articlesList);
        void onDataError(String errorMessage);
    }
}
