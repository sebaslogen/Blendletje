package com.sebaslogen.blendletje.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.modules.MainActivityModule;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.activities.recyclerview.ItemsListAdapter;
import com.sebaslogen.blendletje.ui.presenters.MainContract;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainContract.ViewActions {

    @Inject
    MainContract.UserActions mUserActions;
    private RecyclerView mPopularArticlesRV;
    private Toolbar mToolbar;

    private static void slideEnter(final View view) {
        if (view.getTranslationY() < 0f) {
            view.animate().translationY(0f);
        }
    }

    private static void slideExit(final View view) {
        if (view.getTranslationY() == 0f) {
            view.animate().translationY(-view.getHeight());
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        mToolbar.setTitle(R.string.popular_articles_title);
        mPopularArticlesRV = (RecyclerView) findViewById(R.id.rv_popular_articles_list);
        mPopularArticlesRV.setLayoutManager(new LinearLayoutManager(this));
        attachToScroll(mPopularArticlesRV);

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
    public void displayPopularArticlesList(final List<ListItem> popularArticlesList) {
        mPopularArticlesRV.setVisibility(View.VISIBLE);
        mPopularArticlesRV.setAdapter(new ItemsListAdapter(popularArticlesList));
    }

    private void attachToScroll(final RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                if (dy > 0) {
                    slideExit(mToolbar);
                } else {
                    slideEnter(mToolbar);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}
