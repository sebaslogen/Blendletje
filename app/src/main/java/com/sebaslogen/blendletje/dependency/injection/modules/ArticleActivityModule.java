package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.ui.activities.ArticleActivity;
import com.sebaslogen.blendletje.ui.presenters.ArticleContract;
import com.sebaslogen.blendletje.ui.presenters.ArticlePresenter;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class ArticleActivityModule {

    private final ArticleActivity mArticleActivity;
    private final String mArticleId;

    public ArticleActivityModule(final ArticleActivity mainActivity, final String articleId) {
        mArticleActivity = mainActivity;
        mArticleId = articleId;
    }

    @Provides
    @ActivityScope
    public ArticleActivity provideArticleActivity() {
        return mArticleActivity;
    }

    @Provides
    @ActivityScope
    public ArticlePresenter provideArticleActivityPresenter(@Named("io") final Scheduler ioScheduler,
                                                            @Named("ui") final Scheduler uiScheduler,
            final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        return new ArticlePresenter(mArticleActivity, mArticleId, ioScheduler, uiScheduler,
            requestArticlesCommandBuilder);
    }

    @Provides
    @ActivityScope
    public ArticleContract.UserActions provideArticleActivityUserActions(final ArticlePresenter articlePresenter) {
        return articlePresenter;
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
