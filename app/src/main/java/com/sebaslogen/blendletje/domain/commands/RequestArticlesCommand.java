package com.sebaslogen.blendletje.domain.commands;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.database.DatabaseManager;
import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.domain.mappers.ArticlesDataMapper;
import com.sebaslogen.blendletje.domain.model.Article;

import java.util.List;

import rx.Observable;

/**
 * Command class to handle article requests to different data sources
 * This class behavior is asking local database for data and in the meanwhile
 * update data from the network if possible
 */
public class RequestArticlesCommand {

    private final ArticlesServer mArticlesServer;
    private final DatabaseManager mDatabaseManager;

    public RequestArticlesCommand() {
        mArticlesServer = new ArticlesServer();
        mDatabaseManager = new DatabaseManager();
    }

    public RequestArticlesCommand(final ArticlesServer articlesServer, final DatabaseManager databaseManager) {
        mArticlesServer = articlesServer;
        mDatabaseManager = databaseManager;
    }

    public Observable<List<Article>> getPopularArticles(@Nullable final Integer amount,
                                                        @Nullable final Integer page) {
        final Observable<PopularArticlesResource> popularArticlesObservable = mArticlesServer
                .requestPopularArticles(amount, page)
                .doOnNext(mDatabaseManager::storeObject);
        return popularArticlesObservable.map(ArticlesDataMapper::convertPopularArticlesListToDomain);
    }

    public Observable<Article> getArticle(@NonNull final String id) {
        final Observable<ArticleResource> remoteArticleObservable = getArticleFromRemoteAPI(id);
        final Observable<ArticleResource> localDBArticleObservable = getArticleFromLocalDB(id);
        final Observable<ArticleResource> articleObservable = Observable
                .concat(localDBArticleObservable, remoteArticleObservable).first();
        return articleObservable.map(ArticlesDataMapper::convertArticleToDomain);
    }

    private Observable<ArticleResource> getArticleFromLocalDB(@NonNull final String id) {
        return mDatabaseManager.requestArticle(id);
    }

    @NonNull
    private Observable<ArticleResource> getArticleFromRemoteAPI(@NonNull final String id) {
        return mArticlesServer
                .requestArticle(id)
                .doOnNext(mDatabaseManager::storeObject);
    }

    public static class RequestArticlesCommandBuilder {
        private final ArticlesServer mArticlesServer;
        private final DatabaseManager mDatabaseManager;

        public RequestArticlesCommandBuilder(final ArticlesServer articlesServer,
                                             final DatabaseManager databaseManager) {
            mArticlesServer = articlesServer;
            mDatabaseManager = databaseManager;
        }

        public RequestArticlesCommand createRequestArticlesCommand() {
            return new RequestArticlesCommand(mArticlesServer, mDatabaseManager);
        }
    }
}
