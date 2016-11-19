package com.sebaslogen.blendletje.data.remote;

import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;

import ch.halarious.core.HalResource;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Converter;

final class HALResponseBodyConverter<T extends HalResource> implements Converter<ResponseBody, T>{

    private final Gson gson;

    HALResponseBodyConverter(Gson gson) {
        this.gson = gson;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        BufferedSource source = value.source();
        try {
            String s = source.readString(Charset.forName("UTF-8"));
            //noinspection unchecked
            return (T) gson.fromJson(s, HalResource.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(source);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }
}