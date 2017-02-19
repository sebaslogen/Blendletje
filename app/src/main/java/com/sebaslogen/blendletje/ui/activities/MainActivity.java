package com.sebaslogen.blendletje.ui.activities;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.components.MainActivityComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.MainActivityModule;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.activities.recyclerview.ItemsListAdapter;
import com.sebaslogen.blendletje.ui.presenters.MainContract;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.ViewActions {

    @Inject
    MainContract.UserActions mUserActions;
    @Inject
    ImageLoader mImageLoader;
    private MainActivityComponent mComponent;
    private RecyclerView mPopularArticlesRV;
    private Toolbar mToolbar;
    private Drawable mLoadingAnimationDrawable;
    private ImageView mLoadAnimationView;
    private ItemsListAdapter mItemsListAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        mToolbar.setTitle(R.string.popular_articles_title);
        setSupportActionBar(mToolbar);
        mPopularArticlesRV = (RecyclerView) findViewById(R.id.rv_popular_articles_list);
        mPopularArticlesRV.setLayoutManager(new LinearLayoutManager(this));
        mPopularArticlesRV.setAdapter(new ItemsListAdapter(new ArrayList<>(), mImageLoader,
            (v, i, t, iU) -> null)); // This avoids layout errors
        mLoadAnimationView = (ImageView) findViewById(R.id.iv_animation);
        mLoadingAnimationDrawable = mLoadAnimationView.getDrawable();

        getActivityComponent().inject(this);
    }

    private MainActivityComponent getActivityComponent() {
        if (null != mComponent) {
            return mComponent;
        }
        final Object retainedObject = getLastCustomNonConfigurationInstance();
        if (retainedObject != null) {
            // we are retaining the object, so we can safely cast it to our component class.
            mComponent = (MainActivityComponent) retainedObject;
        } else {
            mComponent = ((BlendletjeApp) getApplication()).getCommandsComponent()
                .plus(new MainActivityModule(this));
        }
        return mComponent;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return getActivityComponent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserActions.attachView(this);
    }

    @Override
    protected void onStop() {
        mUserActions.deAttachView();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mUserActions.onViewDestroyed(isChangingConfigurations());
        super.onDestroy();
    }

    @Override
    public void showLoadingAnimation() {
        mLoadAnimationView.setVisibility(View.VISIBLE);
        if (mLoadingAnimationDrawable instanceof Animatable) {
            final Animatable animationDrawable = (Animatable) mLoadingAnimationDrawable;
            animationDrawable.start();
        }
    }

    @Override
    public void hideLoadingAnimation() {
        if (mLoadingAnimationDrawable instanceof Animatable) {
            final Animatable animationDrawable = (Animatable) mLoadingAnimationDrawable;
            animationDrawable.stop();
        }
        mLoadAnimationView.setVisibility(View.GONE);
    }

    @Override
    public void displayPopularArticlesList(@NonNull final List<ListItem> popularArticlesList) {
        if (mItemsListAdapter == null) {
            mItemsListAdapter = new ItemsListAdapter(popularArticlesList, mImageLoader,
                (view, id, title, imageUrl) -> {
                    openArticle(view, id, title, imageUrl);
                    return null;
                });
            mPopularArticlesRV.setAdapter(mItemsListAdapter);
        } else {
            mItemsListAdapter.updateList(popularArticlesList);
        }
    }

    private void openArticle(final View view, final String id, final String title,
                             @Nullable final String imageUrl) {
        final ImageView imageView = imageUrl == null ? null : (ImageView) view.findViewById(R.id.iv_image);
        final TextView textView = (TextView) view.findViewById(R.id.tv_title);
        ArticleActivity.openArticleActivity(this, imageView, textView, mToolbar, id, title, imageUrl);
    }
}
