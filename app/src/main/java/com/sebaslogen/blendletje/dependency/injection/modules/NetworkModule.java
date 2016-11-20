package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.data.remote.ArticlesServer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.CallAdapter;

@Module
public class NetworkModule {

    private final HttpUrl mBaseUrl;
    private final CallAdapter.Factory mRxAdapter;

    public NetworkModule(final String baseUrl, final CallAdapter.Factory rxAdapter) {
        mBaseUrl = HttpUrl.parse(baseUrl);
        mRxAdapter = rxAdapter;
    }

    public NetworkModule(final HttpUrl baseUrl, final CallAdapter.Factory rxAdapter) {
        mBaseUrl = baseUrl;
        mRxAdapter = rxAdapter;
    }

    @Provides
    @Singleton
    ArticlesServer provideArticlesServer() {
        return new ArticlesServer(mBaseUrl, mRxAdapter);
    }

}
