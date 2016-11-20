package com.sebaslogen.blendletje.data.remote;

import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static com.sebaslogen.blendletje.data.remote.TestUtils.prepareAndStartServerToReturnJsonFromFile;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ArticlesServerTest {

    private MockWebServer mServer;

    @Before
    public void setUp() throws Exception {
        mServer = new MockWebServer(); // Create a MockWebServer. These are lean enough to create an instance for every unit test
    }

    @After
    public void tearDown() throws IOException {
        mServer.shutdown(); // Shut down the mServer. Instances cannot be reused.
    }

    @Test
    public void requestPopularArticles() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final ArticlesServer articlesServer = new ArticlesServer(baseUrl, RxJavaCallAdapterFactory.
                createWithScheduler(Schedulers.immediate()));
        final PopularArticlesResource popularArticlesResource = articlesServer.requestPopularArticles();

        // Then the request is correctly received
        assertThat("No articles loaded", popularArticlesResource.items().size(), greaterThan(0));
    }

    @Test
    public void requestPopularArticlesObservable() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final ArticlesServer articlesServer = new ArticlesServer(baseUrl, RxJavaCallAdapterFactory.
                createWithScheduler(Schedulers.immediate()));
        final Observable<PopularArticlesResource> popularArticlesObservable = articlesServer
                .requestPopularArticles(null, null);
        final TestSubscriber<PopularArticlesResource> testSubscriber = new TestSubscriber<>();
        popularArticlesObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        final List<PopularArticlesResource> events = testSubscriber.getOnNextEvents();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final PopularArticlesResource popularArticles = events.get(0);
        assertThat("No articles loaded", popularArticles.items().size(), greaterThan(0));
    }

}