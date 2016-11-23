package com.sebaslogen.blendletje;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import com.sebaslogen.blendletje.data.remote.apis.BlendleAPI;
import com.sebaslogen.blendletje.dependency.injection.components.CommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.components.DaggerCommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.ApplicationModule;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.DatabaseModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import timber.log.Timber;

public class BlendletjeApp extends Application {

    private static CommandsComponent mCommandsComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        initializeDatabase();
        initializeInjector();
    }

    private void initializeDatabase() {
        Realm.init(this);
    }

    private void initializeInjector() {
        final RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        mCommandsComponent = DaggerCommandsComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(BlendleAPI.END_POINT,
                        RxJavaCallAdapterFactory.create()))
                .databaseModule(new DatabaseModule(realmConfiguration))
                .commandsModule(new CommandsModule())
                .build();
    }

    public CommandsComponent getCommandsComponent() {
        return mCommandsComponent;
    }


    @VisibleForTesting
    public void setComponent(final CommandsComponent commandsComponent) {
        mCommandsComponent = commandsComponent;
    }

}
