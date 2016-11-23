package com.sebaslogen.blendletje.data.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.data.source.ArticlesDataSource;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func0;

/**
 * This class encapsulates all the database read and write access,
 * hiding away the specifics of the DB library used
 */
public class DatabaseManager implements ArticlesDataSource {

    private final Func0<Realm> mDatabaseGetter;
    private final RealmConfiguration mDatabaseConfig;

    public DatabaseManager() {
        mDatabaseConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        mDatabaseGetter = () -> Realm.getInstance(mDatabaseConfig);
    }

    public DatabaseManager(final RealmConfiguration databaseConfig) {
        mDatabaseConfig = databaseConfig;
        mDatabaseGetter = () -> Realm.getInstance(mDatabaseConfig);
    }

    public DatabaseManager(final Func0<Realm> databaseGetter, final RealmConfiguration databaseConfig) {
        mDatabaseConfig = databaseConfig;
        mDatabaseGetter = databaseGetter;
    }

    public void storeObject(final PopularArticlesResource popularArticlesResource) {
        // Try with resources makes sure DB is closed after using it
        try (Realm realm = mDatabaseGetter.call()) {
            realm.executeTransaction(db -> db.copyToRealm(popularArticlesResource));
        }
    }

    public void storeObject(final ArticleResource articleResource) {
        try (Realm realm = mDatabaseGetter.call()) {
            realm.executeTransaction(db -> db.copyToRealm(articleResource));
        }
    }

    @Override
    public PopularArticlesResource requestPopularArticles() throws IOException {
        return null; // TODO: Implement
    }

    @Override
    public Observable<PopularArticlesResource> requestPopularArticles(@Nullable final Integer amount,
                                                                      @Nullable final Integer page) {
        return null; // TODO: Implement
    }

    @Override
    public Observable<ArticleResource> requestArticle(@NonNull final String id) {
        final Realm realm = mDatabaseGetter.call();
        return realm.where(ArticleResource.class)
                .equalTo("id", id) // Search and find object in DB
                .findAllAsync()
                .asObservable()
                .filter(RealmResults::isLoaded)
                .map(realm::copyFromRealm) // Copy as immutable value
                .filter(articles -> !articles.isEmpty()).take(1) // Filter empty results
                .map(articles -> articles.get(0)) // Return only one
                .doOnUnsubscribe(realm::close);
    }
}
