package com.sebaslogen.blendletje.data.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.data.source.ArticlesDataSource;

import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * This class encapsulates all the database read and write access,
 * hiding away the specifics of the DB library used
 */
public class DatabaseManager implements ArticlesDataSource {

    /**
     * This constant indicates the maximum amount of articles to store as cache in the database
     */
    private static final int MAX_ARTICLES_IN_CACHE = 100;
    private final Callable<Realm> mDatabaseGetter;
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

    public DatabaseManager(final Callable<Realm> databaseGetter, final RealmConfiguration databaseConfig) {
        mDatabaseConfig = databaseConfig;
        mDatabaseGetter = databaseGetter;
    }

    /**
     * Store a PopularArticlesResource object containing a list of popular articles
     * This list is stored as a unique object, so every store of a new object will
     * overwrite any existing object.
     *
     * @param popularArticlesResource object containing a list of popular articles to store
     */
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

    /**
     * Store an individual article in the database making sure the cache it's always inside the
     * defined limits of MAX_ARTICLES_IN_CACHE
     * If a version of the article was already stored, it will be overwritten
     *
     * @param articleResource Article to store
     */
    public void storeObject(final ArticleResource articleResource) {
        try (Realm realm = mDatabaseGetter.call()) {
            final RealmResults<ArticleResource> articleResources =
                realm.where(ArticleResource.class).equalTo("id", articleResource.id()).findAll();
            realm.executeTransaction(transaction -> {
                articleResources.deleteAllFromRealm(); // Delete any previous instance of the object in DB
                optimizeCache(realm);
                transaction.copyToRealm(articleResource);
            });
        } catch (final Exception e) {
            Timber.e(e);
        }
    }

    /**
     * When the amount of article items in database exceeds the cache limit,
     * then delete half of the oldest items in  the database
     *
     * @param realm Database from which to check and clear some elements
     */
    private void optimizeCache(final Realm realm) {
        final RealmResults<ArticleResource> articleResources = realm.where(ArticleResource.class).findAll();
        if (articleResources.size() >= MAX_ARTICLES_IN_CACHE) {
            for (int i = 0; i < articleResources.size() / 2; i++) {
                articleResources.deleteFirstFromRealm();
            }
        }
    }

    @Override
    public PopularArticlesResource requestPopularArticles() throws IOException {
        throw new OnErrorNotImplementedException(new Throwable("TODO: implement for not Rx clients"));
    }

    @Override
    public Single<PopularArticlesResource> requestPopularArticles(@Nullable final Integer amount,
                                                                  @Nullable final Integer page) {
        return Single.fromCallable(() -> {
            try (Realm realm = mDatabaseGetter.call()) {
                final RealmResults<PopularArticlesResource> popularArticlesResources =
                    realm.where(PopularArticlesResource.class).findAll();
                if (popularArticlesResources.isEmpty()) {
                    return null; // When no results return null to make Single fail
                }
                return realm.copyFromRealm(popularArticlesResources).get(0); // Copy as immutable value
            }
        });
    }

    @Override
    public Single<ArticleResource> requestArticle(@NonNull final String id) {
        return Single.fromCallable(() -> {
            try (Realm realm = mDatabaseGetter.call()) {
                final RealmResults<ArticleResource> articleResources = // Search and find object in DB
                    realm.where(ArticleResource.class).equalTo("id", id).findAll();
                if (articleResources.isEmpty()) {
                    return null; // When no results return null to make Single fail
                }
                return realm.copyFromRealm(articleResources).get(0); // Copy as immutable value
            }
        });
    }
}
