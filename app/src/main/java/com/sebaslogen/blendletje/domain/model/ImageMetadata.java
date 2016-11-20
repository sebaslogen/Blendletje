package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ImageMetadata {
    abstract String url();
    abstract int width();
    abstract int height();

    public static ImageMetadata create(final String url, final int width, final int height) {
        return new AutoValue_ImageMetadata(url, width, height);
    }
}
