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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        final DatabaseManager databaseManager = mock(DatabaseManager.class);
        when(databaseManager.requestPopularArticles(anyInt(), anyInt()))
            .thenReturn(Single.error(new NullPointerException()));

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                        createWithScheduler(Schedulers.trampoline())),
                databaseManager);
        final Observable<List<Article>> popularArticlesObservable = command.getPopularArticles(null, null);
        final TestObserver<List<Article>> testObserver = new TestObserver<>();
        popularArticlesObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.await(2, TimeUnit.SECONDS);
        final List<Object> events = testObserver.getEvents().get(0);
        testObserver.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final List<Article> popularArticles = (List<Article>) events.get(0);
        assertThat("No articles loaded", popularArticles.size(), greaterThan(0));
    }

    @Test
    public void getPopularArticles_storesArticlesInDB() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "popular(ws.blendle.com_items_popular).json");
        final DatabaseManager databaseManager = mock(DatabaseManager.class);
        when(databaseManager.requestPopularArticles(anyInt(), anyInt()))
            .thenReturn(Single.error(new NullPointerException()));

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                        createWithScheduler(Schedulers.trampoline())),databaseManager);
        final Observable<List<Article>> popularArticlesObservable = command.getPopularArticles(null, null);
        final TestObserver<List<Article>> testObserver = new TestObserver<>();
        popularArticlesObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.assertNoErrors();
        verify(databaseManager).storeObject(any(PopularArticlesResource.class));
    }

    @Test
    public void getArticle_returnsRequestedArticleFromTheServer() throws Exception {
        // Given there is a web server with some prepared responses
        final String articleId = "bnl-vkn-20161117-7352758";
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "article(ws.blendle.com_item_" + articleId + ").json");
        final DatabaseManager databaseManager = mock(DatabaseManager.class);
        when(databaseManager.requestArticle(anyString()))
            .thenReturn(Single.error(new NullPointerException()));

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                        createWithScheduler(Schedulers.trampoline())),
                databaseManager);
        final Single<Article> articleObservable = command.getArticle(articleId);
        final TestObserver<Article> testObserver = new TestObserver<>();
        articleObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.await(2, TimeUnit.SECONDS);
        final List<Object> events = testObserver.getEvents().get(0);
        testObserver.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final Article article = (Article) events.get(0);
        assertEquals(articleId, article.id());
    }

    @Test
    public void getArticle_storesArticleInDB() throws Exception {
        // Given there is a web server with some prepared responses
        final String articleId = "bnl-vkn-20161117-7352758";
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer,
                "article(ws.blendle.com_item_" + articleId + ").json");
        final DatabaseManager databaseManager = mock(DatabaseManager.class);
        when(databaseManager.requestArticle(anyString()))
            .thenReturn(Single.error(new NullPointerException()));

        // When I make a request
        final RequestArticlesCommand command = new RequestArticlesCommand(
                new ArticlesServer(baseUrl, RxJava2CallAdapterFactory.
                        createWithScheduler(Schedulers.trampoline())), databaseManager);
        final Single<Article> articleObservable = command.getArticle(articleId);
        final TestObserver<Article> testObserver = new TestObserver<>();
        articleObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.assertNoErrors();
        verify(databaseManager).storeObject(any(ArticleResource.class));
    }

    // TODO: Add negative test cases and add hermetic unit test cases mocking layers below
}