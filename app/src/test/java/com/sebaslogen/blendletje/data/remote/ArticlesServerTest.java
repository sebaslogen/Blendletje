package com.sebaslogen.blendletje.data.remote;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static utils.TestUtils.prepareAndStartServerToReturnJsonFromFile;

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
    public void requestPopularArticles_returnsObjectWithArticles() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final ArticlesServer articlesServer = new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                createWithScheduler(Schedulers.trampoline()));
        final PopularArticlesResource popularArticlesResource = articlesServer.requestPopularArticles();

        // Then the request is correctly received
        assertThat("No articles loaded", popularArticlesResource.items().size(), greaterThan(0));
    }

    @Test
    public void requestPopularArticlesObservable_returnsObjectWithArticles() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final ArticlesServer articlesServer = new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                createWithScheduler(Schedulers.trampoline()));
        final Single<PopularArticlesResource> popularArticlesObservable = articlesServer
                .requestPopularArticles(null, null);
        final TestObserver<PopularArticlesResource> testObserver = new TestObserver<>();
        popularArticlesObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.await(2, TimeUnit.SECONDS);
        final List<Object> events = testObserver.getEvents().get(0);
        testObserver.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final PopularArticlesResource popularArticles = (PopularArticlesResource) events.get(0);
        assertThat("No articles loaded", popularArticles.items().size(), greaterThan(0));
    }

    @Test
    public void requestArticleObservable_returnsObjectWithArticle() throws Exception {
        // Given there is a web server with some prepared responses
        final String articleId = "bnl-vkn-20161117-7352758";
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "article(ws.blendle.com_item_" + articleId + ").json");

        // When I make a request
        final ArticlesServer articlesServer = new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                createWithScheduler(Schedulers.trampoline()));
        final Single<ArticleResource> articleObservable = articlesServer
                .requestArticle(articleId);
        final TestObserver<ArticleResource> testObserver = new TestObserver<>();
        articleObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.await(2, TimeUnit.SECONDS);
        final List<Object> events = testObserver.getEvents().get(0);
        testObserver.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final ArticleResource article = (ArticleResource) events.get(0);
        assertEquals(articleId, article.id());
    }

    // TODO: Add negative test cases
}