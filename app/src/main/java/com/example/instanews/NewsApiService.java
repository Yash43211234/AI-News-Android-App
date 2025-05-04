package com.example.instanews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsApiService {
    @GET
    Call<NewsResponse> getNews(@Url String url);
}
