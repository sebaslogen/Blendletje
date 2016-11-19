package com.sebaslogen.blendletje.data.remote.apis;

import com.sebaslogen.blendletje.data.remote.HALConverterFactory;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.sebaslogen.blendletje.data.remote.TestUtils.readResourcesFile;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BlendleAPITest {

    private MockWebServer mServer;

    @Before
    public void setUp() throws Exception {
        setupTestServer();
    }

    @After
    public void tearDown() throws IOException {
        mServer.shutdown(); // Shut down the mServer. Instances cannot be reused.
    }

    @Test
    public void requestPopularArticlesFromServer_parsesListOfArticlesFromJsonResponse() throws Exception {
        // Given there is a web server with some prepared responses
        final HttpUrl baseUrl = prepareAndStartServerToReturnJsonFromFile(mServer, "popular(ws.blendle.com_items_popular).json");

        // When I make a web request
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .build();
        final BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);

        // Then I get a response and the response is parsed correctly
        final Response<PopularArticlesResource> response = blendleAPI.popularArticles().execute();
        assertTrue(response.isSuccessful());
        final RecordedRequest request1 = mServer.takeRequest();
        assertEquals("/items/popular", request1.getPath());
        final List<ArticleResource> articles = response.body().items();
        assertThat("No articles loaded", articles.size(), greaterThan(0));
        final ArticleResource firstArticle = articles.get(0);
        assertThat("No text contents loaded for first article", firstArticle.manifest().body().size(), greaterThan(0));
        assertThat("No images information loaded for first article", firstArticle.manifest().images().size(), greaterThan(0));
        assertNotNull("No small image information loaded for first article", firstArticle.manifest().images().get(0)._links().small());


    }

    private HttpUrl prepareAndStartServerToReturnJsonFromFile(final MockWebServer mockWebServer, final String fileName) throws IOException {
        final String responseBody = readResourcesFile(fileName);
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(responseBody));
        mockWebServer.start();
        // Ask the mServer for its URL. You'll need this to make HTTP requests.
        return mockWebServer.url("/");
    }

    private void setupTestServer() throws IOException {
        // Create a MockWebServer. These are lean enough to create an instance for every unit test
        mServer = new MockWebServer();
    }
}