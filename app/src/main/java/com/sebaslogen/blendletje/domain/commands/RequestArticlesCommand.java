package com.sebaslogen.blendletje.domain.commands;

import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.domain.mappers.ArticlesDataMapper;
import com.sebaslogen.blendletje.domain.model.Article;

import java.util.List;

import rx.Observable;

public class RequestArticlesCommand {

    final private ArticlesServer mArticlesServer;

    public RequestArticlesCommand() {
        mArticlesServer = new ArticlesServer();
    }

    public RequestArticlesCommand(final ArticlesServer articlesServer) {
        mArticlesServer = articlesServer;
    }

    public Observable<List<Article>> getPopularArticles(@Nullable final Integer amount,
                                                        @Nullable final Integer page) {
        final Observable<PopularArticlesResource> popularArticlesObservable = mArticlesServer
                .requestPopularArticles(amount, page);
        return popularArticlesObservable.map(ArticlesDataMapper::convertPopularArticlesListToDomain);
    }
}
