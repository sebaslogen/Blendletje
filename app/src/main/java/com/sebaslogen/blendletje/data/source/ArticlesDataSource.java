package com.sebaslogen.blendletje.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import java.io.IOException;

import io.reactivex.Single;


public interface ArticlesDataSource {

    PopularArticlesResource requestPopularArticles() throws IOException;

    Single<PopularArticlesResource> requestPopularArticles(@Nullable Integer amount,
                                                           @Nullable Integer page);

    Single<ArticleResource> requestArticle(@NonNull String id);
}
