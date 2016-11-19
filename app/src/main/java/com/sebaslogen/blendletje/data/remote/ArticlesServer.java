package com.sebaslogen.blendletje.data.remote;

import com.sebaslogen.blendletje.data.remote.apis.BlendleAPI;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.data.source.ArticlesDataSource;

import java.io.IOException;

import retrofit2.Retrofit;
import rx.Observable;

public class ArticlesServer implements ArticlesDataSource {

    @Override
    public PopularArticlesResource requestPopularArticles() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BlendleAPI.END_POINT)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .build();
        BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);

        // Then I get a response and the response is parsed correctly
        return blendleAPI.popularArticles().execute().body();
    }

    @Override
    public Observable<PopularArticlesResource> requestPopularArticlesObservable(int amount,
                                                                                int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BlendleAPI.END_POINT)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .build();
        BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);
        return blendleAPI.popularArticlesObservable(amount, page);
    }
}
