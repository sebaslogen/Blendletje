package com.sebaslogen.blendletje.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import ch.halarious.core.HalDeserializer;
import ch.halarious.core.HalExclusionStrategy;
import ch.halarious.core.HalResource;
import ch.halarious.core.HalSerializer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class HALConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static HALConverterFactory create(final Class<?> type) {
        return new HALConverterFactory(type);
    }

    private HALConverterFactory(final Class<?> type) {
        if (!HalResource.class.isAssignableFrom(type))
            throw new NullPointerException("Type should be a subclass of HalResource");
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(HalResource.class, new HalSerializer());
        builder.registerTypeAdapter(HalResource.class, new HalDeserializer(type));
        builder.setExclusionStrategies(new HalExclusionStrategy());
        this.gson = builder.create();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(final Type type,
                                                            final Annotation[] annotations,
                                                            final Retrofit retrofit) {
        return new HALResponseBodyConverter<>(gson);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(final Type type,
                                                          final Annotation[] parameterAnnotations,
                                                          final Annotation[] methodAnnotations,
                                                          final Retrofit retrofit) {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}