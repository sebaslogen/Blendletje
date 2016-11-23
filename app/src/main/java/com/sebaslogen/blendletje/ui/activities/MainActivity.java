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
    private RecyclerView mPopularArticlesRV;
    private Drawable mLoadingAnimationDrawable;
    private ImageView mLoadAnimationView;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        toolbar.setTitle(R.string.popular_articles_title);
        setSupportActionBar(toolbar);
        mPopularArticlesRV = (RecyclerView) findViewById(R.id.rv_popular_articles_list);
        mPopularArticlesRV.setLayoutManager(new LinearLayoutManager(this));
        mPopularArticlesRV.setAdapter(new ItemsListAdapter(new ArrayList<>(), mImageLoader,
                (v, i, t, iU) -> null)); // This avoids layout errors
        mLoadAnimationView = (ImageView) findViewById(R.id.iv_animation);
        mLoadingAnimationDrawable = mLoadAnimationView.getDrawable();

        ((BlendletjeApp) getApplication()).getCommandsComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserActions.attachView();
    }

    @Override
    protected void onPause() {
        mUserActions.deAttachView();
        super.onPause();
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
        // TODO: Use payload to notify changes instead of recreating
        mPopularArticlesRV.setAdapter(new ItemsListAdapter(popularArticlesList, mImageLoader,
                (view, id, title, imageUrl) -> {
                    openArticle(view, id, title, imageUrl);
                    return null;
                }));
    }

    private void openArticle(final View view, final String id, final String title,
                             @Nullable final String imageUrl) {
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        final TextView titleView = (TextView) view.findViewById(R.id.tv_title);
        ArticleActivity.openArticleActivity(this, imageView, titleView, id, title, imageUrl);
//        ArticleActivity.openArticleActivity(this, imageView, titleView, id, title, "https://static.blendle.nl/publication/newyorktimes/2016/11/22/item/36_1/version/1/image/large/3a7bcb130f14ad332178c95ee32440395bdd999d.jpg");
    }
}
