package com.sebaslogen.blendletje.dependency.injection.components;

import com.sebaslogen.blendletje.dependency.injection.modules.ApplicationModule;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.DatabaseModule;
import com.sebaslogen.blendletje.dependency.injection.modules.MainActivityModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        DatabaseModule.class,
        CommandsModule.class,
        NetworkModule.class })
public interface CommandsComponent {

    MainActivityComponent plus(MainActivityModule module);
}
