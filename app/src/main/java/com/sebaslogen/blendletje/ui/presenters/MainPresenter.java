package com.sebaslogen.blendletje.ui.presenters;

import android.util.Log;

import com.sebaslogen.blendletje.BuildConfig;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.domain.model.Article;

import java.util.List;

import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

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
        mViewActions.showTitle("Hola Blendle!");
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
                .observeOn(getUIScheduler())
                .subscribe(this::showArticles,
                        throwable -> {
                            // TODO: Handle error loading in UI
                            if (BuildConfig.DEBUG) {
                                Log.e(MainPresenter.class.getSimpleName(),
                                        "Error loading list of articles loaded and thrown to UI",
                                        throwable);
                            }
                        });
        mSubscriptions.add(subscription);
    }

    Scheduler getUIScheduler() {
        return AndroidSchedulers.mainThread();
    }

    private void showArticles(final List<Article> articles) {
        // TODO: Show articles in UI
        if (BuildConfig.DEBUG) {
            Log.d(MainPresenter.class.getSimpleName(), "List of articles loaded and thrown to UI");
        }
    }
}
