package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class ActivityModule {

    @Provides
    @ActivityScope
    @Named("io")
    public static Scheduler provideRxJavaIOScheduler() {
        return Schedulers.io();
    }

    @Provides
    @ActivityScope
    @Named("ui")
    public static Scheduler getUIScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
