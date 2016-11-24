package com.sebaslogen.blendletje.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.sebaslogen.blendletje.R;
import com.squareup.picasso.Picasso;

public class ArticleActivity extends AppCompatActivity {
    public static final String ARTICLE_ID = "ARTICLE_ID";
    public static final String EXTRA_ARTICLE_TITLE = "EXTRA_ARTICLE_TITLE";
    public static final String EXTRA_ARTICLE_IMAGE = "EXTRA_ARTICLE_IMAGE";

    //    @Inject
//    MainContract.UserActions mUserActions;
//    @Inject
//    ImageLoader mImageLoader;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    /**
     * Method to navigate and animate transition to this screen
     *
     * @param activity  Caller activity from which to transition into this one
     * @param imageView Shared image view between the two screens
     * @param id        Id of the article to show in this screen
     * @param title     Title of the article to show in this screen
     * @param imageUrl  Url of the main image to show in this screen
     */
    public static void openArticleActivity(final AppCompatActivity activity, final ImageView imageView,
                                           final TextView titleView, final String id,
                                           final String title, @Nullable final String imageUrl) {
        final Intent intent = new Intent(activity, ArticleActivity.class);
        intent.putExtra(ArticleActivity.ARTICLE_ID, id);
        intent.putExtra(ArticleActivity.EXTRA_ARTICLE_TITLE, title);
        intent.putExtra(ArticleActivity.EXTRA_ARTICLE_IMAGE, imageUrl);
        final View decor = activity.getWindow().getDecorView();
        final View navigationBar = decor.findViewById(android.R.id.navigationBarBackground);
        final View statusBar = decor.findViewById(android.R.id.statusBarBackground);
        final Pair<View, String> imagePair = Pair.create(imageView, ArticleActivity.EXTRA_ARTICLE_IMAGE + id);
        final Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
        final Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
        @SuppressWarnings("unchecked") final ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, navPair, statusPair, imagePair);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityTransitions();
        setContentView(R.layout.activity_article);
        supportPostponeEnterTransition();

        final String id = getIntent().getStringExtra(ARTICLE_ID);
        final String imageUrl = getIntent().getStringExtra(EXTRA_ARTICLE_IMAGE);
        if (imageUrl != null) {
            ViewCompat.setTransitionName(findViewById(R.id.iv_article), EXTRA_ARTICLE_IMAGE + id);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar_article);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctbl_toolbar);
        mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat
                .getColor(this, android.R.color.transparent));
        final String articleTitle = getIntent().getStringExtra(EXTRA_ARTICLE_TITLE);
        mCollapsingToolbarLayout.setTitle(articleTitle);
        final TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(articleTitle);

        final ImageView imageView = (ImageView) findViewById(R.id.iv_article);
        final int primary = ContextCompat.getColor(this, R.color.colorPrimary);
        final int primaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        if (imageUrl != null) {
            Picasso.with(this).load(imageUrl)
                    .placeholder(R.drawable.empty)
                    .error(R.drawable.empty)
                    .into(imageView, PicassoPalette.with(imageUrl, imageView)
                            .use(PicassoPalette.Profile.MUTED)
                            .intoCallBack(palette ->
                                    mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary)))
                            .use(PicassoPalette.Profile.MUTED_DARK)
                            .intoCallBack(palette -> {
                                mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
                                supportStartPostponedEnterTransition();
                            })
                    );
        } else {
            imageView.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void setupActivityTransitions() {
        final Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);
    }
}
