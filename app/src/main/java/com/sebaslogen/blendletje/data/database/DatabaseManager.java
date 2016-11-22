package com.sebaslogen.blendletje.data.database;

import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.functions.Func0;

/**
 * This class encapsulates all the database read and write access,
 * hiding away the specifics of the DB library used
 */
public class DatabaseManager {

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
}
