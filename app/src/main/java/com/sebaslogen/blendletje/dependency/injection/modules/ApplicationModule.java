package com.sebaslogen.blendletje.dependency.injection.modules;

import android.content.Context;

import com.sebaslogen.blendletje.ui.utils.ImageLoader;

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

    @Singleton
    @Provides
    public ImageLoader provideImageLoader() {
        return new ImageLoader(mAppContext);
    }

}
