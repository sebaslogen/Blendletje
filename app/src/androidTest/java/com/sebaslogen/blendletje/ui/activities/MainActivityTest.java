package com.sebaslogen.blendletje.ui.activities;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.linkedin.android.testbutler.TestButler;
import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.components.CommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.ApplicationModule;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.DatabaseModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.pages.ArticlePage;
import com.sebaslogen.blendletje.ui.pages.MainPage;
import com.sebaslogen.blendletje.ui.presenters.ArticlePresenter;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;
import com.squareup.picasso.Picasso;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import io.realm.RealmConfiguration;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;
import utils.MockDataProvider;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final BlendletjeApp mApp = (BlendletjeApp) InstrumentationRegistry.getInstrumentation()
        .getTargetContext()
        .getApplicationContext();
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class,
        false,  // initialTouchMode
        false); // launchActivity: false to set intent per test
    @Rule
    public DaggerMockRule<CommandsComponent> daggerRule =
        new DaggerMockRule<>(CommandsComponent.class,
            new ApplicationModule(mApp),
            new NetworkModule("http://mock.domain", RxJavaCallAdapterFactory.
                createWithScheduler(Schedulers.immediate())),
            new DatabaseModule(mock(RealmConfiguration.class)),
            new CommandsModule())
            .set(mApp::setComponent);
    // @Mock annotated fields are injected automatically by DaggerMockRule instead of @Provides methods
    @Mock
    ImageLoader mImageLoader;
    @Mock
    MainPresenter mMainPresenter;
    @Mock
    ArticlePresenter mArticlePresenter;

    public MainActivityTest() throws IOException {
    }

    @Before
    public void setUp() throws Exception {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestButler.verifyAnimationsDisabled(targetContext);
        // Load local Logo drawable resource for all image load requests
        final Picasso picasso = Picasso.with(mApp);
        doReturn(picasso.load(R.drawable.logo)).when(mImageLoader).load(anyString());
        doAnswer(invocation -> {
            picasso.cancelRequest((ImageView) invocation.getArguments()[0]);
            return null;
        }).when(mImageLoader).cancelRequest(any(ImageView.class));
    }

    @Test
    public void onDataLoaded_loadingAnimationDisappears() throws Exception {
        // Given I open the app
        final MainActivity mainActivity = activityTestRule.launchActivity(null);

        // When the presenter hides the animation
        mainActivity.runOnUiThread(mainActivity::hideLoadingAnimation);

        // Then the animation is not visible anymore
        final MainPage mainPage = new MainPage();
        mainPage.checkLoadingAnimationIsNotShown();
    }

    @Test
    public void onDataLoaded_listOfPopularArticlesIsShownWithArticle() throws Exception {
        // Given I open the app
        final MainActivity mainActivity = activityTestRule.launchActivity(null);
        final List<ListItem> popularArticlesList = MockDataProvider.provideMockedDomainListOfListItem();

        // When I load a list of articles
        mainActivity.runOnUiThread(() -> mainActivity.displayPopularArticlesList(popularArticlesList));

        // Then the same list of articles is shown
        final MainPage mainPage = new MainPage();
        mainPage.checkArticleItemsAreShown(popularArticlesList);
    }

    @Test
    public void tapOnArticle_opensArticle() throws Exception {
        // Given a list of articles is displayed
        final MainActivity mainActivity = activityTestRule.launchActivity(null);
        final List<ListItem> popularArticlesList = MockDataProvider.provideMockedDomainListOfListItem();
        mainActivity.runOnUiThread(() -> mainActivity.displayPopularArticlesList(popularArticlesList));
        final Article article = (Article) popularArticlesList.get(0);

        // When I click on one
        final MainPage mainPage = new MainPage();
        final ArticlePage articlePage = mainPage.openArticle(0);

        // Then the article is opened
        articlePage.checkTitleIs(article.contents().title());
    }

    @Test
    public void tappingBackOnArticle_goesBackToList() throws Exception {
        // Given I navigate to an article
        final MainActivity mainActivity = activityTestRule.launchActivity(null);
        final List<ListItem> popularArticlesList = MockDataProvider.provideMockedDomainListOfListItem();
        mainActivity.runOnUiThread(() -> mainActivity.displayPopularArticlesList(popularArticlesList));
        final MainPage mainPage = new MainPage();
        final ArticlePage articlePage = mainPage.openArticle(0);

        // When I press back
        final MainPage returningPage = articlePage.pressBack();

        // Then the article is opened
        returningPage.checkArticleItemsAreShown(popularArticlesList);
    }

    // TODO: Add negative test cases and add hermetic unit test cases mocking layers below
}