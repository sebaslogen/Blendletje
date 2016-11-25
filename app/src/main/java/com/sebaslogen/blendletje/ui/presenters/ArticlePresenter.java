package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.domain.model.Advertisement;
import com.sebaslogen.blendletje.domain.model.Article;

import javax.inject.Named;

import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class ArticlePresenter implements ArticleContract.UserActions {

    private final ArticleContract.ViewActions mViewActions;
    private String mArticleId;
    private final Scheduler mIOScheduler;
    private final Scheduler mUIScheduler;
    private final CompositeSubscription mSubscriptions;
    private final RequestArticlesCommand.RequestArticlesCommandBuilder
        mRequestArticlesCommandBuilder;
    private boolean mIsDataLoaded = false;

    public ArticlePresenter(final ArticleContract.ViewActions viewActions, final String articleId,
                            @Named("io") final Scheduler ioScheduler,
                            @Named("ui") final Scheduler uiScheduler,
                            final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        mViewActions = viewActions;
        mArticleId = articleId;
        mIOScheduler = ioScheduler;
        mUIScheduler = uiScheduler;
        mSubscriptions = new CompositeSubscription();
        mRequestArticlesCommandBuilder = requestArticlesCommandBuilder;
    }

    @Override public void attachView() {
        if (!mIsDataLoaded) {
            mViewActions.showLoadingAnimation();
            loadArticle();
        }
    }

    @Override public void deAttachView() {
        mSubscriptions.clear(); // Unsubscribe from any ongoing subscription
    }

    private void loadArticle() {
        final Subscription subscription =
            mRequestArticlesCommandBuilder.createRequestArticlesCommand()
                .getArticle(mArticleId)
                .subscribeOn(mIOScheduler)
                .observeOn(mUIScheduler)
                .subscribe(this::showArticle, throwable -> {
                    // TODO: Handle error loading in UI
                    Timber.e(throwable, "Error loading article with id %s", mArticleId);
                });
        mSubscriptions.add(subscription);
    }

    private void showArticle(final Article article) {
        Timber.d("Article loaded and thrown to UI");
        mIsDataLoaded = true;
        mViewActions.hideLoadingAnimation();
        mViewActions.displayArticle(article);
        mViewActions.displayAdvertisement(new Advertisement("Bring me home!"));
    }
}
