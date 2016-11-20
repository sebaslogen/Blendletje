package com.sebaslogen.blendletje;

import android.app.Application;

import com.sebaslogen.blendletje.data.remote.apis.BlendleAPI;
import com.sebaslogen.blendletje.dependency.injection.components.CommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.components.DaggerCommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class BlendletjeApp extends Application {

    private static CommandsComponent mCommandsComponent;

    @Override public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        this.initializeInjector();
    }

    private void initializeInjector() {
        mCommandsComponent = DaggerCommandsComponent.builder()
                .networkModule(new NetworkModule(BlendleAPI.END_POINT,
                        RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())))
                .commandsModule(new CommandsModule())
                .build();
    }

    public CommandsComponent getCommandsComponent() {
        return mCommandsComponent;
    }

}
