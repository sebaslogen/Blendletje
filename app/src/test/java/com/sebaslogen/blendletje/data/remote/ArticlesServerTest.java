package com.sebaslogen.blendletje.data.remote;

import com.sebaslogen.blendletje.data.remote.apis.BlendleAPI;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ArticlesServerTest {

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
        HttpUrl baseUrl = prepareServerToReturnJsonFromFile("popular(ws.blendle.com_items_popular).json");

        // When I make a web request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(HALConverterFactory.create(PopularArticlesResource.class))
                .build();
        BlendleAPI blendleAPI = retrofit.create(BlendleAPI.class);

        // Then I get a response and the response is parsed correctly
        final Response<PopularArticlesResource> response = blendleAPI.popularArticles().execute();
        assertTrue(response.isSuccessful());
        RecordedRequest request1 = mServer.takeRequest();
        assertEquals("/items/popular", request1.getPath());
        List<ArticleResource> articles = response.body().items();
        assertThat("No articles loaded", articles.size(), greaterThan(0));
        ArticleResource firstArticle = articles.get(0);
        assertThat("No text contents loaded for first article", firstArticle.manifest().body().size(), greaterThan(0));
        assertThat("No images information loaded for first article", firstArticle.manifest().images().size(), greaterThan(0));
        assertNotNull("No small image information loaded for first article", firstArticle.manifest().images().get(0)._links().small());


    }

    private HttpUrl prepareServerToReturnJsonFromFile(final String fileName) throws IOException {
        String responseBody = readResourcesFile(fileName);
        mServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(responseBody));
        mServer.start();
        // Ask the mServer for its URL. You'll need this to make HTTP requests.
        return mServer.url("/");
    }

    private void setupTestServer() throws IOException {
        // Create a MockWebServer. These are lean enough to create an instance for every unit test
        mServer = new MockWebServer();
    }

    private String readResourcesFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}