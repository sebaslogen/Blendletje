package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.ArrayList;
import java.util.List;

import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainPresenter implements MainContract.UserActions {

    private final MainContract.ViewActions mViewActions;
    private final Scheduler mIOScheduler;
    private final CompositeSubscription mSubscriptions;
    private final RequestArticlesCommand.RequestArticlesCommandBuilder mRequestArticlesCommandBuilder;

    public MainPresenter(final MainContract.ViewActions viewActions,
                         final Scheduler ioScheduler,
                         final RequestArticlesCommand.RequestArticlesCommandBuilder requestArticlesCommandBuilder) {
        mViewActions = viewActions;
        mIOScheduler = ioScheduler;
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
        mSubscriptions.clear(); // Unsubscribe from any ongoing subscription
    }

    private void loadPopularArticles() {
        final Subscription subscription = mRequestArticlesCommandBuilder
                .createRequestArticlesCommand()
                .getPopularArticles(null, null)
                .map(this::addAdvertisements)
                .subscribeOn(mIOScheduler)
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

    Scheduler getUIScheduler() {
        return AndroidSchedulers.mainThread();
    }

    private void showArticles(final List<ListItem> items) {
        Timber.d("List of articles loaded and thrown to UI");
        mViewActions.hideLoadingAnimation();
        mViewActions.displayPopularArticlesList(items);
    }
}
