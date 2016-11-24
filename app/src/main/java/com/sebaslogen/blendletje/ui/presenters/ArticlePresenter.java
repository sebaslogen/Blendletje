package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import javax.inject.Named;
import rx.Scheduler;
import rx.subscriptions.CompositeSubscription;

public class ArticlePresenter implements ArticleContract.UserActions {

    private final ArticleContract.ViewActions mViewActions;
    private final Scheduler mIOScheduler;
    private final Scheduler mUIScheduler;
    private final CompositeSubscription mSubscriptions;
    private final RequestArticlesCommand.RequestArticlesCommandBuilder
        mRequestArticlesCommandBuilder;
    private boolean mIsDataLoaded = false;

    public ArticlePresenter(final ArticleContract.ViewActions viewActions,
                            @Named("io") final Scheduler ioScheduler,
                            @Named("ui") final Scheduler uiScheduler,
                            final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        mViewActions = viewActions;
        mIOScheduler = ioScheduler;
        mUIScheduler = uiScheduler;
        mSubscriptions = new CompositeSubscription();
        mRequestArticlesCommandBuilder = requestArticlesCommandBuilder;
    }

    @Override public void attachView() {
        if (!mIsDataLoaded) {
            //mViewActions.showLoadingAnimation();
            //loadArticle();
        }
    }

    @Override public void deAttachView() {
        mSubscriptions.clear(); // Unsubscribe from any ongoing subscription
    }
}
