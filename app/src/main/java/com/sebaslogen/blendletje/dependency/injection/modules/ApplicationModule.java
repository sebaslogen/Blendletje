package com.sebaslogen.blendletje.dependency.injection.modules;

import android.content.Context;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class ApplicationModule {

    private final Context mAppContext;

    public ApplicationModule(final Context context) {
        mAppContext = context.getApplicationContext();
    }

    @Singleton
    @Provides
    public Context provideApplicationContext() {
        return mAppContext;
    }

    @Singleton
    @Provides
    public ImageLoader provideImageLoader() {
        return new ImageLoader(mAppContext);
    }

    @Provides
    @ActivityScope
    @Named("io")
    public Scheduler provideRxJavaIOScheduler() {
        return Schedulers.io();
    }

    @Provides
    @ActivityScope
    @Named("ui")
    public Scheduler getUIScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
