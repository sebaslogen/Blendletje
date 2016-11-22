package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class TestUtils {

    /**
     * Read a JSON file from disk and enqueue it as the response contents of the given mock web server
     * @param mockWebServer Server in which to enqueue the JSON response
     * @param fileName Name of the JSON file to load from disk
     * @return Base URL to contact the mocked web server
     * @throws IOException In case something went wrong reading the file from disk
     */
    public static HttpUrl prepareAndStartServerToReturnJsonFromFile(
            final MockWebServer mockWebServer, final String fileName) throws IOException {
        final String responseBody = readResourcesFile(fileName);
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(responseBody));
        mockWebServer.start();
        // Ask the mServer for its URL. You'll need this to make HTTP requests.
        return mockWebServer.url("/");
    }

    /**
     * This method can only load a resource placed in the same package as this java file
     * inside the test resources folder
     * @param fileName Name of the file to read from disk
     * @return Text representation of the contents of the file
     * @throws IOException In case something went wrong reading the file from disk
     */
    private static String readResourcesFile(final String fileName) throws IOException {
        final InputStream inputStream = TestUtils.class.getResourceAsStream(fileName);
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
