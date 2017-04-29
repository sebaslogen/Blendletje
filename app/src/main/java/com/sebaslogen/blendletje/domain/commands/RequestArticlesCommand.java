package com.sebaslogen.blendletje.domain.commands;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.database.DatabaseManager;
import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.domain.mappers.ArticlesDataMapper;
import com.sebaslogen.blendletje.domain.model.Article;

import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;


/**
 * Command class to handle article requests to different data sources
 * The behavior is to ask local database for data and in the meanwhile
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

    /**
     * This method retrieves an observable that emits lists of popular articles
     * Data is fetched first from the database until the first data is received from the network
     * In the case there are network errors the errors will be ignored and local data wil be emitted
     * The resulting data is mapped from teh remote/database layers to the domain layer requirements
     *
     * @param amount Number of articles to fetch
     * @param page   Number of the page of results to fetch
     * @return An observable that will emit lists of articles, usually one from DB and one from network
     */
    public Observable<List<Article>> getPopularArticles(@Nullable final Integer amount,
                                                        @Nullable final Integer page) {
        final Single<PopularArticlesResource>
                remotePopularArticlesObservable = getPopularArticlesFromRemoteAPI(amount, page);
        final Observable<PopularArticlesResource>
                localDBPopularArticlesObservable = getPopularArticlesFromLocalDB(amount, page);
        final Observable<PopularArticlesResource> popularArticlesObservable =
                getPopularArticlesFromCombinedSources(remotePopularArticlesObservable,
                        localDBPopularArticlesObservable);
        return popularArticlesObservable
                .distinct() // Avoid emitting twice the same list of items
                .map(ArticlesDataMapper::convertPopularArticlesListToDomain); // Map data to domain
    }

    /**
     * The behaviour of this method is to take two sources of the same type of observable items
     * and emit them following these steps
     * 1- Publish a shared observable, N, from the original network observable and subscribe to it
     * 2- Simultaneously subscribe to the local database observable L and merge the results of L and N
     * 3- Subscription to L will conditionally stop as soon as any event (item, error or completion)
     * is emitted by other observable N
     * 4- Only in the case a network error stops the stream of N, then observable L will be emitted
     * unconditionally
     *
     * @param remotePopularArticlesSingle  Remote observable to fetch results from the network
     * @param localDBPopularArticlesSingle Local observable to fetch results from the database
     * @return New observable combining both sources of information
     */
    @NonNull
    private Observable<PopularArticlesResource> getPopularArticlesFromCombinedSources(
            final Single<PopularArticlesResource> remotePopularArticlesSingle,
            final Observable<PopularArticlesResource> localDBPopularArticlesSingle) {
        return remotePopularArticlesSingle.toObservable().publish(remotePopularArticles ->
                Observable.merge(remotePopularArticles, // Merge network and local
                        localDBPopularArticlesSingle // but stop local as soon as network emits
                                .takeUntil(remotePopularArticles)))
                .onErrorResumeNext(throwable -> (throwable instanceof UnknownHostException) ?
                        localDBPopularArticlesSingle : Observable.error(throwable));
    }

    private Observable<PopularArticlesResource> getPopularArticlesFromLocalDB(final Integer amount,
                                                                              final Integer page) {
        return mDatabaseManager.requestPopularArticles(amount, page).toObservable().onErrorResumeNext(throwable -> {
            if (throwable instanceof NullPointerException) {
                return Observable.empty();
            } else {
                throw Exceptions.propagate(throwable);
            }
        });
    }

    @NonNull
    private Single<PopularArticlesResource> getPopularArticlesFromRemoteAPI(
            @Nullable final Integer amount, @Nullable final Integer page) {
        return mArticlesServer
                .requestPopularArticles(amount, page)
                .doOnSuccess(mDatabaseManager::storeObject);
    }

    /**
     * This method retrieves an observable that emits the requested article if available or error otherwise
     * Data is fetch first from the database and, only if data is not available, it's requested from the network
     *
     * @param id Unique identifier of the requested article
     * @return An observable that will emit one article or error if not found locally neither remotely
     */
    public Single<Article> getArticle(@NonNull final String id) {
        final Observable<ArticleResource> remoteArticleObservable = getArticleFromRemoteAPI(id).toObservable();
        final Observable<ArticleResource> localDBArticleObservable = getArticleFromLocalDB(id);
        final Single<ArticleResource> articleObservable = Observable
                .concat(localDBArticleObservable, remoteArticleObservable).firstOrError();
        return articleObservable.map(ArticlesDataMapper::convertArticleToDomain);
    }

    private Observable<ArticleResource> getArticleFromLocalDB(@NonNull final String id) {
        return mDatabaseManager.requestArticle(id).toObservable().onErrorResumeNext(throwable -> {
            if (throwable instanceof NullPointerException) {
                return Observable.empty();
            } else {
                throw Exceptions.propagate(throwable);
            }
        });
    }

    @NonNull
    private Single<ArticleResource> getArticleFromRemoteAPI(@NonNull final String id) {
        return mArticlesServer
                .requestArticle(id)
                .doOnSuccess(mDatabaseManager::storeObject);
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
