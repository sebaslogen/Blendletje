package com.sebaslogen.blendletje.ui.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.domain.model.Advertisement;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainPresenter implements MainContract.UserActions {

    private final Scheduler mIOScheduler;
    private final Scheduler mUIScheduler;
    private final CompositeSubscription mSubscriptions;
    private final RequestArticlesCommand.RequestArticlesCommandBuilder mRequestArticlesCommandBuilder;
    @NonNull
    private MainContract.ViewActions mViewActions;
    private boolean mIsViewAttached;
    private boolean mIsDataShown = false;
    @Nullable private Observable<List<ListItem>> mPopularArticlesObservableCache;

    public MainPresenter(@NonNull final MainContract.ViewActions viewActions,
                         @Named("io") final Scheduler ioScheduler,
                         @Named("ui") final Scheduler uiScheduler,
                         final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        mViewActions = viewActions;
        mIsViewAttached = false;
        mIOScheduler = ioScheduler;
        mUIScheduler = uiScheduler;
        mSubscriptions = new CompositeSubscription();
        mRequestArticlesCommandBuilder = requestArticlesCommandBuilder;
    }

    @Override
    public void attachView(final MainContract.ViewActions viewActions) {
        mViewActions = viewActions;
        mIsViewAttached = true;
        if (!mIsDataShown) {
            mViewActions.showLoadingAnimation();
            loadPopularArticles();
        }
    }

    @Override
    public void deAttachView() {
        mIsViewAttached = false;
    }

    @Override
    public void onViewDestroyed(final boolean andRecreating) {
        if (andRecreating) {
            mIsDataShown = false;
        } else {
            mSubscriptions.clear(); // Unsubscribe from any ongoing subscription
        }
    }

    private void loadPopularArticles() {
        final Subscription subscription =
            getPopularArticlesObservable()
                .subscribeOn(mIOScheduler)
                .observeOn(mUIScheduler)
                .subscribe(articles -> {
                    if (mIsViewAttached) {
                        showArticles(articles);
                    }
                }, throwable -> {
                    // TODO: Handle error loading in UI
                    Timber.e(throwable, "Error loading list of articles");
                });
        mSubscriptions.add(subscription);
    }

    @NonNull
    private Observable<List<ListItem>> getPopularArticlesObservable() {
        if (mPopularArticlesObservableCache == null) {
            mPopularArticlesObservableCache = mRequestArticlesCommandBuilder
                .createRequestArticlesCommand()
                .getPopularArticles(null, null)
                .map(this::addAdvertisements)
                .cache();
        }
        return mPopularArticlesObservableCache;
    }

    private List<ListItem> addAdvertisements(final List<Article> articles) {
        final List<ListItem> items = new ArrayList<>(articles.size());
        try { // Fake delay caused by loading advertisements
            Thread.sleep(700 + (new Random()).nextInt(1000));
        } catch (final InterruptedException ignored) {
        }
        final Random randomGenerator = new Random();
        for (final Article article : articles) {
            if (randomGenerator.nextInt(10) % 3 == 0) {
                items.add(new Advertisement("Buy now, or later, or never!"));
            }
            items.add(article);
        }
        return items;
    }

    private void showArticles(final List<ListItem> items) {
        Timber.d("List of articles loaded and thrown to UI");
        mIsDataShown = true;
        mViewActions.hideLoadingAnimation();
        mViewActions.displayPopularArticlesList(items);
    }
}
