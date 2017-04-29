package com.sebaslogen.blendletje.data.remote.apis;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BlendleAPI {

    String END_POINT = "https://ws.blendle.com";

    @GET("items/popular")
    Call<PopularArticlesResource> popularArticles();

    @GET("items/popular")
    Single<PopularArticlesResource> popularArticlesObservable(@Query("amount") Integer amount,
                                                              @Query("page") Integer page);

    @GET("item/{id}")
    Single<ArticleResource> articlesObservable(@Path("id") String id);
}
