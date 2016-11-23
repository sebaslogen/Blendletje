package com.sebaslogen.blendletje.domain.commands;

import com.sebaslogen.blendletje.data.database.DatabaseManager;
import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
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
import rx.Single;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static utils.TestUtils.prepareAndStartServerToReturnJsonFromFile;

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
    public void getPopularArticles_returnsListOfArticles() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJavaCallAdapterFactory.
                        createWithScheduler(Schedulers.immediate())),
                mock(DatabaseManager.class));
        final Observable<List<Article>> popularArticlesObservable = command.getPopularArticles(null, null);
        final TestSubscriber<List<Article>> testSubscriber = new TestSubscriber<>();
        popularArticlesObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        final List<List<Article>> events = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final List<Article> popularArticles = events.get(0);
        assertThat("No articles loaded", popularArticles.size(), greaterThan(0));
    }

    @Test
    public void getPopularArticles_storesArticlesInDB() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");

        // When I make a request
        final DatabaseManager databaseManager = mock(DatabaseManager.class);
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJavaCallAdapterFactory.
                        createWithScheduler(Schedulers.immediate())),databaseManager);
        final Observable<List<Article>> popularArticlesObservable = command.getPopularArticles(null, null);
        final TestSubscriber<List<Article>> testSubscriber = new TestSubscriber<>();
        popularArticlesObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        testSubscriber.assertNoErrors();
        verify(databaseManager).storeObject(any(PopularArticlesResource.class));
    }

    @Test
    public void getArticle_returnsRequestedArticleFromTheServer() throws Exception {
        // Given there is a web server with some prepared responses
        final String articleId = "bnl-vkn-20161117-7352758";
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "article(ws.blendle.com_item_" + articleId + ").json");

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJavaCallAdapterFactory.
                        createWithScheduler(Schedulers.immediate())),
                mock(DatabaseManager.class));
        final Single<Article> articleObservable = command.getArticle(articleId);
        final TestSubscriber<Article> testSubscriber = new TestSubscriber<>();
        articleObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        final List<Article> events = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final Article article = events.get(0);
        assertEquals(articleId, article.id());
    }

    @Test
    public void getArticle_storesArticleInDB() throws Exception {
        // Given there is a web server with some prepared responses
        final String articleId = "bnl-vkn-20161117-7352758";
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "article(ws.blendle.com_item_" + articleId + ").json");

        // When I make a request
        final DatabaseManager databaseManager = mock(DatabaseManager.class);
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJavaCallAdapterFactory.
                        createWithScheduler(Schedulers.immediate())),databaseManager);
        final Single<Article> articleObservable = command.getArticle(articleId);
        final TestSubscriber<Article> testSubscriber = new TestSubscriber<>();
        articleObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        testSubscriber.assertNoErrors();
        verify(databaseManager).storeObject(any(ArticleResource.class));
    }

    // TODO: Add negative test cases and add hermetic unit test cases mocking layers below
}