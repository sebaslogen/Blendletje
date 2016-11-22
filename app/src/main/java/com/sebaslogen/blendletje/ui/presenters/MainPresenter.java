package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainPresenter implements MainContract.UserActions {

    private final MainContract.ViewActions mViewActions;
    private final CompositeSubscription mSubscriptions;
    private final RequestArticlesCommand.RequestArticlesCommandBuilder mRequestArticlesCommandBuilder;

    public MainPresenter(final MainContract.ViewActions viewActions,
                         final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        mViewActions = viewActions;
        mSubscriptions = new CompositeSubscription();
        mRequestArticlesCommandBuilder = requestArticlesCommandBuilder;
    }

    @Override
    public void attachView() {
        mViewActions.showLoadingAnimation();
        loadPopularArticles();
    }

    @Override
    public void deAttachView() {
        mSubscriptions.clear();
    }

    private void loadPopularArticles() {
        final Subscription subscription = mRequestArticlesCommandBuilder
                .createRequestArticlesCommand()
                .getPopularArticles(null, null)
                .map(this::addAdvertisements)
                .subscribeOn(getIOScheduler())
                .observeOn(getUIScheduler())
                .subscribe(this::showArticles,
                        throwable -> {
                            // TODO: Handle error loading in UI
                            Timber.e(throwable,
                                    "Error loading list of articles loaded and thrown to UI");
                        });
        mSubscriptions.add(subscription);
    }

    private List<ListItem> addAdvertisements(final List<Article> articles) {
        final List<ListItem> items = new ArrayList<>(articles);
        try { // Fake delay caused by loading advertisements
            Thread.sleep(1500);
        } catch (final InterruptedException ignored) {
        }
        // TODO: Fill items list with advertisements
        return items;
    }

    Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    Scheduler getUIScheduler() {
        return AndroidSchedulers.mainThread();
    }

    private void showArticles(final List<ListItem> items) {
        Timber.d("List of articles loaded and thrown to UI");
        mViewActions.hideLoadingAnimation();
        mViewActions.displayPopularArticlesList(items);
    }
}
