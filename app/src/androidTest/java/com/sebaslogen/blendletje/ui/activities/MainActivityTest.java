package com.sebaslogen.blendletje.ui.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.dependency.injection.components.CommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.ApplicationModule;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.DatabaseModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.pages.MainPage;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;
import com.sebaslogen.blendletje.ui.utils.SystemAnimations;
import com.squareup.picasso.Picasso;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.util.List;

import io.realm.RealmConfiguration;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;
import utils.MockDataProvider;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static utils.TestUtils.prepareAndStartServerToReturnJsonFromFile;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final BlendletjeApp mApp = (BlendletjeApp) InstrumentationRegistry.getInstrumentation()
            .getTargetContext()
            .getApplicationContext();
    private final MockWebServer mServer = new MockWebServer();
    private final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
            "popular(ws.blendle.com_items_popular).json");
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class,
            false,  // initialTouchMode
            false); // launchActivity: false to set intent per test
    @Rule
    public DaggerMockRule<CommandsComponent> daggerRule =
            new DaggerMockRule<>(CommandsComponent.class,
                    new ApplicationModule(mApp),
                    new NetworkModule(baseUrl, RxJavaCallAdapterFactory.
                            createWithScheduler(Schedulers.immediate())),
                    new DatabaseModule(mock(RealmConfiguration.class)),
                    new CommandsModule())
                    .set(mApp::setComponent);
    // @Mock annotated fields are injected automatically by DaggerMockRule instead of @Provides methods
    @Mock
    ImageLoader mImageLoader;
    @Mock
    MainPresenter mMainPresenter;
    private SystemAnimations mSystemAnimations;

    public MainActivityTest() throws IOException {
    }

    @Before
    public void setUp() throws Exception {
        mSystemAnimations = new SystemAnimations(InstrumentationRegistry.getInstrumentation()
                .getTargetContext());
        mSystemAnimations.disableAll();
        final Picasso picasso = Picasso.with(mApp);
        doReturn(picasso.load(R.drawable.logo)).when(mImageLoader).load(anyString());
        doAnswer(invocation -> {
            picasso.cancelRequest((ImageView) invocation.getArguments()[0]);
            return null;
        }).when(mImageLoader).cancelRequest(any(ImageView.class));
    }

    @After
    public void tearDown() throws Exception {
        mSystemAnimations.enableAll();
    }

    @Test
    public void onDataLoaded_loadingAnimationDisappears() throws Exception {
        // Given
        final MainActivity mainActivity = activityTestRule.launchActivity(null);
        // When
        mainActivity.runOnUiThread(mainActivity::hideLoadingAnimation);
        // Then
        final MainPage mainPage = new MainPage();
        mainPage.checkLoadingAnimationIsNotShown();
    }

    @Test
    public void onDataLoaded_listOfPopularArticlesIsShownWithArticle() throws Exception {
        // Given
        final MainActivity mainActivity = activityTestRule.launchActivity(null);
        final List<ListItem> popularArticlesList = MockDataProvider.provideMockedDomainListOfListItem();
        // When
        mainActivity.runOnUiThread(() -> mainActivity.displayPopularArticlesList(
                popularArticlesList));
        // Then
        final MainPage mainPage = new MainPage();
        mainPage.checkArticleItemsAreShown(popularArticlesList);
    }

    // TODO: Add negative test cases and add hermetic unit test cases mocking layers below
}