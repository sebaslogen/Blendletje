package com.sebaslogen.blendletje.ui.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.test.runner.AndroidJUnitRunner;

import java.lang.reflect.Method;

import timber.log.Timber;

/**
 * Disable animations so that they do not interfere with Espresso tests.
 * <p/>
 * Source: https://code.google.com/p/android-test-kit/wiki/DisablingAnimations
 */
public final class SystemAnimations extends AndroidJUnitRunner {

    private static final String ANIMATION_PERMISSION = "android.permission.SET_ANIMATION_SCALE";
    private static final float DISABLED = 0.0f;
    private static final float DEFAULT = 1.0f;

    private final Context mContext;

    public SystemAnimations(final Context context) {
        this.mContext = context;
    }

    public void disableAll() {
        final int permStatus = mContext.checkCallingOrSelfPermission(ANIMATION_PERMISSION);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(DISABLED);
        }
    }

    public void enableAll() {
        final int permStatus = mContext.checkCallingOrSelfPermission(ANIMATION_PERMISSION);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(DEFAULT);
        }
    }

    private static void setSystemAnimationsScale(final float animationScale) {
        try {
            final Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            final Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            final Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            final Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            final Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            final Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            final Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            final IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            final Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            final float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
        } catch (final Exception e) {
            Timber.e("Could not change animation scale to %s :'(", animationScale);
        }
    }
}