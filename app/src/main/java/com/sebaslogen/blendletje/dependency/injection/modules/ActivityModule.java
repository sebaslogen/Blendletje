package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class ActivityModule {

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
