package com.sebaslogen.blendletje.domain.commands;

import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.domain.mappers.ArticlesDataMapper;
import com.sebaslogen.blendletje.domain.model.Article;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Observable;

public class RequestArticlesCommand {

    final private ArticlesServer mArticlesServer;
//    final private Realm mDatabaseManager; // TODO: Use interface

    public RequestArticlesCommand() {
        mArticlesServer = new ArticlesServer();
//        mDatabaseManager = Realm.getDefaultInstance();
    }

    public RequestArticlesCommand(final ArticlesServer articlesServer) {
        mArticlesServer = articlesServer;
//        mDatabaseManager = Realm.getDefaultInstance(); // TODO: Inject
    }

    public Observable<List<Article>> getPopularArticles(@Nullable final Integer amount,
                                                        @Nullable final Integer page) {
        final Observable<PopularArticlesResource> popularArticlesObservable = mArticlesServer
                .requestPopularArticles(amount, page)
                .map(this::storeInDB);
//                .doOnNext(this::storeInDB);
        return popularArticlesObservable.map(ArticlesDataMapper::convertPopularArticlesListToDomain);
    }

    private PopularArticlesResource storeInDB(final PopularArticlesResource popularArticlesResource) {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm mDatabaseManager = Realm.getInstance(config);
        mDatabaseManager.beginTransaction();
        mDatabaseManager.copyToRealm(popularArticlesResource);
        mDatabaseManager.commitTransaction();


        PopularArticlesResource result = mDatabaseManager.where(PopularArticlesResource.class)
                .findFirst();
        return result;

    }

    public static class RequestArticlesCommandBuilder
    {
        private final ArticlesServer mArticlesServer;

        public RequestArticlesCommandBuilder(final ArticlesServer articlesServer)
        {
            mArticlesServer = articlesServer;
        }

        public RequestArticlesCommand createRequestArticlesCommand()
        {
            return new RequestArticlesCommand(mArticlesServer);
        }
    }
}
