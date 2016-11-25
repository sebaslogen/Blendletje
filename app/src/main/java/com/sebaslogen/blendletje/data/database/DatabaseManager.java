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
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Func0;
import timber.log.Timber;

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
            realm.executeTransaction(transaction -> {
                realm.delete(PopularArticlesResource.class);
                transaction.copyToRealm(popularArticlesResource);
            });
        } catch (final Exception error) {
            Timber.e(error, "Error storing PopularArticlesResource into database");
        }
    }

    public void storeObject(final ArticleResource articleResource) {
        try (Realm realm = mDatabaseGetter.call()) {
            // TODO: Store same object (by id) only once
            realm.executeTransaction(db -> db.copyToRealm(articleResource));
        }
    }

    @Override
    public PopularArticlesResource requestPopularArticles() throws IOException {
        throw new OnErrorNotImplementedException(new Throwable("TODO: implement for not Rx clients"));
    }

    @Override
    public Observable<PopularArticlesResource> requestPopularArticles(@Nullable final Integer amount,
                                                                      @Nullable final Integer page) {
        return Observable.fromCallable((Func0<PopularArticlesResource>) () -> {
            try (Realm realm = mDatabaseGetter.call()) {
                RealmResults<PopularArticlesResource> popularArticlesResources =
                        realm.where(PopularArticlesResource.class).findAll();
                if (popularArticlesResources.isEmpty()) {
                    return null; // When no results return null and filter it out at the end of this method
                }
                return realm.copyFromRealm(popularArticlesResources).get(0); // Copy as immutable value
            }
        }).filter(popularArticlesResource -> popularArticlesResource != null);
    }

    @Override
    public Observable<ArticleResource> requestArticle(@NonNull final String id) {
        return Observable.fromCallable((Func0<ArticleResource>) () -> {
            try (Realm realm = mDatabaseGetter.call()) {
                RealmResults<ArticleResource> articleResources =
                        realm.where(ArticleResource.class)
                                .equalTo("id", id) // Search and find object in DB
                                .findAll();
                if (articleResources.isEmpty()) {
                    return null;
                }
                return realm.copyFromRealm(articleResources).get(0); // Copy as immutable value
            }
        }).filter(popularArticlesResource -> popularArticlesResource != null);
    }
}
