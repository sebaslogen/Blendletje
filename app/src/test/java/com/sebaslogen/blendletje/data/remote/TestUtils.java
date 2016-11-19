package com.sebaslogen.blendletje.data.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

    public static String readResourcesFile(final String fileName) throws IOException {
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
