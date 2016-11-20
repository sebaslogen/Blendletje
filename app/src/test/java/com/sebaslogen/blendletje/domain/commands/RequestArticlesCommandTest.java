package com.sebaslogen.blendletje.domain.commands;

import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.domain.model.Article;

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

public class RequestArticlesCommandTest {

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
    public void getPopularArticles() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl httpUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(httpUrl, RxJavaCallAdapterFactory.
                        createWithScheduler(Schedulers.immediate())));
        final Observable<List<Article>> popularArticlesObservable = command.getPopularArticles(null, null);
        final TestSubscriber<List<Article>> testSubscriber = new TestSubscriber<>();
        popularArticlesObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        final List<List<Article>> events = testSubscriber.getOnNextEvents();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final List<Article> popularArticles = events.get(0);
        assertThat("No articles loaded", popularArticles.size(), greaterThan(0));
    }

}