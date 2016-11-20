package com.sebaslogen.blendletje.ui.activities;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.modules.MainActivityModule;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.activities.recyclerview.ItemsListAdapter;
import com.sebaslogen.blendletje.ui.presenters.MainContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.ViewActions {

    @Inject
    MainContract.UserActions mUserActions;
    private RecyclerView mPopularArticlesRV;
    private Drawable mLoadingAnimationDrawable;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        toolbar.setTitle(R.string.popular_articles_title);
        setSupportActionBar(toolbar);
        mPopularArticlesRV = (RecyclerView) findViewById(R.id.rv_popular_articles_list);
        mPopularArticlesRV.setLayoutManager(new LinearLayoutManager(this));
        mPopularArticlesRV.setAdapter(new ItemsListAdapter(new ArrayList<>())); // This avoids layout errors
        mLoadingAnimationDrawable = ((ImageView) findViewById(R.id.iv_animation)).getDrawable();

        ((BlendletjeApp) getApplication()).getCommandsComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserActions.attachView();
        startLoadingAnimation();
    }

    private void startLoadingAnimation() {
        if (mLoadingAnimationDrawable instanceof Animatable) {
            final Animatable animationDrawable = (Animatable) mLoadingAnimationDrawable;
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        mUserActions.deAttachView();
        super.onPause();
    }

    @Override
    public void displayPopularArticlesList(final List<ListItem> popularArticlesList) {
        mPopularArticlesRV.setAdapter(new ItemsListAdapter(popularArticlesList));
    }
}
