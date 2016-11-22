package com.sebaslogen.blendletje.ui.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sebaslogen.blendletje.BlendletjeApp;
import com.sebaslogen.blendletje.dependency.injection.components.CommandsComponent;
import com.sebaslogen.blendletje.dependency.injection.modules.CommandsModule;
import com.sebaslogen.blendletje.dependency.injection.modules.DatabaseModule;
import com.sebaslogen.blendletje.dependency.injection.modules.NetworkModule;
import com.sebaslogen.blendletje.ui.pages.MainPage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.realm.RealmConfiguration;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static utils.TestUtils.prepareAndStartServerToReturnJsonFromFile;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

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

    public MainActivityTest() throws IOException {
    }

    @Test
    public void onOpen_loadingAnimationIsShown() throws Exception {
        activityTestRule.launchActivity(null);
        new MainPage();
    }

    @Test
    public void onDataLoaded_loadingAnimationDisappears() throws Exception {

    }

    @Test
    public void onOpen_listOfPopularArticlesIsShown() throws Exception {

    }

}