package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.ui.activities.MainActivity;
import com.sebaslogen.blendletje.ui.presenters.MainContract;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;
import com.sebaslogen.blendletje.ui.utils.IOScheduler;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;

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
    public IOScheduler provideIOScheduler() {
        return new IOScheduler();
    }

    @Provides
    @ActivityScope
    public Scheduler provideRxJavaIOScheduler(final IOScheduler ioScheduler) {
        return ioScheduler.get();
    }
}
