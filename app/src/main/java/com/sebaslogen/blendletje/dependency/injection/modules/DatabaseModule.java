package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.data.database.DatabaseManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

@Module
public class DatabaseModule {

    private final RealmConfiguration mDatabaseConfig;

    public DatabaseModule(final RealmConfiguration databaseConfig) {
        mDatabaseConfig = databaseConfig;
    }

    @Provides
    @Singleton
    public DatabaseManager providesDatabaseManager() {
        return new DatabaseManager(mDatabaseConfig);
    }
}
