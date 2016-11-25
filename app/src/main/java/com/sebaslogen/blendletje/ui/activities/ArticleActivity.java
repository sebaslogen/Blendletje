package com.sebaslogen.blendletje.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.modules.ArticleActivityModule;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ArticleContent;
import com.sebaslogen.blendletje.ui.presenters.ArticleContract;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;
import com.sebaslogen.blendletje.ui.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ArticleActivity extends AppCompatActivity implements ArticleContract.ViewActions {
    public static final String ARTICLE_ID = "ARTICLE_ID";
    public static final String EXTRA_ARTICLE_TITLE = "EXTRA_ARTICLE_TITLE";
    public static final String EXTRA_ARTICLE_IMAGE = "EXTRA_ARTICLE_IMAGE";
    private static final String TOOLBAR_TRANSITION_NAME = "TOOLBAR_TRANSITION_NAME";

    @Inject
    ArticleContract.UserActions mUserActions;
    @Inject
    ImageLoader mImageLoader;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImageView;
    private Drawable mLoadingAnimationDrawable;
    private ImageView mLoadAnimationView;
    private TextView mTextContentView;

    /**
     * Method to navigate and animate transition to this screen
     *
     * @param activity    Caller activity from which to transition into this one
     * @param imageView   Shared image view between the two screens
     * @param toolbarView Shared toolbar view between the two screens
     * @param id          Id of the article to show in this screen
     * @param title       Title of the article to show in this screen
     * @param imageUrl    Url of the main image to show in this screen
     */
    public static void openArticleActivity(final AppCompatActivity activity,
                                           @Nullable final ImageView imageView,
                                           final TextView textView,
                                           final Toolbar toolbarView,
                                           final String id, final String title,
                                           @Nullable final String imageUrl) {
        final Intent intent = new Intent(activity, ArticleActivity.class);
        intent.putExtra(ARTICLE_ID, id);
        intent.putExtra(EXTRA_ARTICLE_TITLE, title);
        intent.putExtra(EXTRA_ARTICLE_IMAGE, imageUrl);
        @SuppressWarnings("unchecked") final ActivityOptionsCompat options =
            getActivityTransitionOptions(activity, imageView, textView, toolbarView, id);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
//        ActivityCompat.startActivity(activity, intent, null);
    }

    @NonNull
    private static ActivityOptionsCompat getActivityTransitionOptions(final AppCompatActivity activity,
                                                                      @Nullable final ImageView imageView,
                                                                      final TextView textView,
                                                                      final Toolbar toolbarView,
                                                                      final String articleId) {
        final List<Pair<View, String>> pairs = new ArrayList<>();
        final View decor = activity.getWindow().getDecorView();
        final View navigationBar = decor.findViewById(android.R.id.navigationBarBackground);
        final View statusBar = decor.findViewById(android.R.id.statusBarBackground);
        pairs.add(Pair.create(textView, EXTRA_ARTICLE_TITLE + articleId));
        pairs.add(Pair.create(imageView, EXTRA_ARTICLE_IMAGE + articleId));
        pairs.add(Pair.create(toolbarView, TOOLBAR_TRANSITION_NAME + articleId));
        pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        final List<Pair<View, String>> validPairs = new ArrayList<>();
        for (final Pair<View, String> pair : pairs) {
            if (pair.first != null) { // On some Android phones the navigation or status views are null
                validPairs.add(pair);
            }
        }
        //noinspection unchecked
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
            validPairs.toArray(new Pair[validPairs.size()]));
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityTransitions();
        setContentView(R.layout.activity_article);
        supportPostponeEnterTransition();

        final String articleId = getIntent().getStringExtra(ARTICLE_ID);
        final String imageUrl = getIntent().getStringExtra(EXTRA_ARTICLE_IMAGE);
        final TextView title = (TextView) findViewById(R.id.tv_title);
        mImageView = (ImageView) findViewById(R.id.iv_article);
        if (imageUrl != null) {
            ViewCompat.setTransitionName(mImageView, EXTRA_ARTICLE_IMAGE + articleId);
        }
        ViewCompat.setTransitionName(title, EXTRA_ARTICLE_TITLE + articleId);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar_article);
        ViewCompat.setTransitionName(toolbar, TOOLBAR_TRANSITION_NAME + articleId);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctbl_toolbar);
        mCollapsingToolbarLayout.setExpandedTitleColor(
            ContextCompat.getColor(this, android.R.color.transparent));
        final String articleTitle = getIntent().getStringExtra(EXTRA_ARTICLE_TITLE);
        mCollapsingToolbarLayout.setTitle(articleTitle);
        title.setText(TextUtils.getSpannedString(articleTitle));
        mLoadAnimationView = (ImageView) findViewById(R.id.iv_animation);
        mLoadingAnimationDrawable = mLoadAnimationView.getDrawable();
        mTextContentView = (TextView) findViewById(R.id.tv_content);

        ((BlendletjeApp) getApplication()).getCommandsComponent()
            .plus(new ArticleActivityModule(this, articleId))
            .inject(this);

        if (imageUrl != null) {
            loadMainImage(imageUrl);
        } else {
            supportStartPostponedEnterTransition();
        }
    }

    private void loadMainImage(final String imageUrl) {
        final int primary = ContextCompat.getColor(this, R.color.colorPrimary);
        final int primaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        mImageLoader.load(imageUrl)
            .placeholder(R.drawable.empty)
            .error(R.drawable.empty)
            .into(mImageView, PicassoPalette.with(imageUrl, mImageView)
                .use(PicassoPalette.Profile.MUTED)
                .intoCallBack(palette -> mCollapsingToolbarLayout.setContentScrimColor(
                    palette.getMutedColor(primary)))
                .use(PicassoPalette.Profile.MUTED_DARK)
                .intoCallBack(palette -> {
                    mCollapsingToolbarLayout.setStatusBarScrimColor(
                        palette.getDarkMutedColor(primaryDark));
                    supportStartPostponedEnterTransition();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserActions.attachView();
    }

    @Override
    protected void onPause() {
        mImageLoader.cancelRequest(mImageView);
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
    public void displayArticle(final Article article) {
        final ArticleContent contents = article.contents();
        if (contents != null) {
            CharSequence sequence = "";
            for (final String paragraph : contents.paragraphs()) {
                sequence = android.text.TextUtils.concat(sequence, "<p>", paragraph);
            } // Twice to fill the screen with more fake content
            for (final String paragraph : contents.paragraphs()) {
                sequence = android.text.TextUtils.concat(sequence, "<p>", paragraph);
            }
            mTextContentView.setText(TextUtils.getSpannedString(sequence.toString()));
            mTextContentView.setVisibility(View.VISIBLE);
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
