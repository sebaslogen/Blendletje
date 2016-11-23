package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.ui.activities.MainActivity;
import com.sebaslogen.blendletje.ui.presenters.MainContract;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module
public class MainActivityModule {

    private final MainActivity mainActivity;

    public MainActivityModule(final MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity provideMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    public MainPresenter provideMainActivityPresenter(final Scheduler ioScheduler,
            final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        return new MainPresenter(mainActivity, ioScheduler, requestArticlesCommandBuilder);
    }

    @Provides
    @ActivityScope
    public MainContract.UserActions provideMainActivityUserActions(final MainPresenter mainPresenter) {
        return mainPresenter;
    }

    @Provides
    @ActivityScope
    public Scheduler provideIOScheduler() {
        return Schedulers.io();
    }
}
