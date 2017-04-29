package com.sebaslogen.blendletje.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.apis.BlendleAPI;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.data.source.ArticlesDataSource;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Class implementing access to data through a network API to provide
 * lists of popular articles and individual articles
 */
public class ArticlesServer implements ArticlesDataSource {

    private final HttpUrl mBaseUrl;
    private final CallAdapter.Factory mRxAdapter;

    public ArticlesServer() {
        mBaseUrl = HttpUrl.parse(BlendleAPI.END_POINT);
        mRxAdapter = RxJava2CallAdapterFactory.create();
    }

    public ArticlesServer(final HttpUrl baseUrl, final CallAdapter.Factory rxAdapter) {
        mBaseUrl = baseUrl;
        mRxAdapter = rxAdapter;
    }

    @Override
    public PopularArticlesResource requestPopularArticles() throws IOException {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .build();
        final BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);
        return blendleAPI.popularArticles().execute().body();
    }

    @Override
    public Single<PopularArticlesResource> requestPopularArticles(@Nullable final Integer amount,
                                                                 @Nullable final Integer page) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .addCallAdapterFactory(mRxAdapter)
                .build();
        final BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);
        return blendleAPI.popularArticlesObservable(amount, page);
    }

    @Override
    public Single<ArticleResource> requestArticle(@NonNull final String id) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(HALConverterFactory.create(ArticleResource.class))
                .addCallAdapterFactory(mRxAdapter)
                .build();
        final BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);
        return blendleAPI.articlesObservable(id);
    }
}
