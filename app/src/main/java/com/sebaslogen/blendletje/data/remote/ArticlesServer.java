package com.sebaslogen.blendletje.data.remote;

import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.apis.BlendleAPI;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.data.source.ArticlesDataSource;

import java.io.IOException;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ArticlesServer implements ArticlesDataSource {

    private final CallAdapter.Factory mRxAdapter;

    public ArticlesServer() {
        mRxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    public ArticlesServer(final CallAdapter.Factory rxAdapter) {
        mRxAdapter = rxAdapter;
    }

    @Override
    public PopularArticlesResource requestPopularArticles() throws IOException {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BlendleAPI.END_POINT)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .build();
        final BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);

        // Then I get a response and the response is parsed correctly
        return blendleAPI.popularArticles().execute().body();
    }

    @Override
    public Observable<PopularArticlesResource> requestPopularArticlesObservable(@Nullable final Integer amount,
                                                                                @Nullable final Integer page) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BlendleAPI.END_POINT)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .addCallAdapterFactory(mRxAdapter)
                .build();
        final BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);
        return blendleAPI.popularArticlesObservable(amount, page);
    }
}
