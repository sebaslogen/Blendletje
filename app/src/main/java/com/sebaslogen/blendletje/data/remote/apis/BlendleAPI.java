package com.sebaslogen.blendletje.data.remote.apis;

import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BlendleAPI {

    String ENDPOINT = "https://ws.blendle.com";

    @GET("items/popular")
    Call<PopularArticlesResource> popularArticles();

    @GET("test")
    Call<String> testCall();
}
