package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.ui.activities.MainActivity;
import com.sebaslogen.blendletje.ui.presenters.MainContract;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

@Module(includes = MainActivityModule.Declarations.class)
public class MainActivityModule extends ActivityModule {

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
    public MainPresenter provideMainActivityPresenter(@Named("io") final Scheduler ioScheduler,
                                                      @Named("ui") final Scheduler uiScheduler,
            final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        return new MainPresenter(mainActivity, ioScheduler, uiScheduler, requestArticlesCommandBuilder);
    }

    @Module
    public interface Declarations {
        @Binds
        @ActivityScope
        public abstract MainContract.UserActions provideMainActivityUserActions(final MainPresenter mainPresenter);
    }
}
