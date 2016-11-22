package com.sebaslogen.blendletje.dependency.injection.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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

}
