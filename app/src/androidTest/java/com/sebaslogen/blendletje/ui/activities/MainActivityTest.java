package com.sebaslogen.blendletje.ui.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.dependency.injection.components.CommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.DatabaseModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.pages.MainPage;
import com.sebaslogen.blendletje.ui.presenters.MainPresenter;
import com.sebaslogen.blendletje.ui.utils.SystemAnimations;

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

import static org.mockito.Mockito.mock;
import static utils.TestUtils.prepareAndStartServerToReturnJsonFromFile;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final MockWebServer mServer = new MockWebServer();
    private final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
            "popular(ws.blendle.com_items_popular).json");
    private SystemAnimations mSystemAnimations;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class,
            false,  // initialTouchMode
            false); // launchActivity: false to set intent per test
    @Rule
    public DaggerMockRule<CommandsComponent> daggerRule =
            new DaggerMockRule<>(CommandsComponent.class,
                    new NetworkModule(baseUrl, RxJavaCallAdapterFactory.
                            createWithScheduler(Schedulers.immediate())),
                    new DatabaseModule(mock(RealmConfiguration.class)),
                    new CommandsModule())
                    .set(
                            component -> {
                                final BlendletjeApp app =
                                        (BlendletjeApp) InstrumentationRegistry.getInstrumentation()
                                                .getTargetContext()
                                                .getApplicationContext();
                                app.setComponent(component);
                            });

    // Injected automatically by DaggerMockRule instead of @Provides methods
    @Mock
    MainPresenter mMainPresenter;

    @Before
    public void setUp() throws Exception {
        mSystemAnimations = new SystemAnimations(InstrumentationRegistry.getInstrumentation()
                .getTargetContext());
        mSystemAnimations.disableAll();
    }

    @After
    public void tearDown() throws Exception {
        mSystemAnimations.enableAll();
    }

    public MainActivityTest() throws IOException {
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
        mainActivity.runOnUiThread(() -> {
            mainActivity.displayPopularArticlesList(
                    popularArticlesList);
        });
        // Then
        final MainPage mainPage = new MainPage();
        mainPage.checkArticleItemsAreShown(popularArticlesList);
    }

    // TODO: Add negative test cases and add hermetic unit test cases mocking layers below
}