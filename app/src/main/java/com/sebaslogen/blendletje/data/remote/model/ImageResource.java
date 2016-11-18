package com.sebaslogen.blendletje.data.remote.model;

import com.google.auto.value.AutoValue;

import ch.halarious.core.HalResource;

@AutoValue
public abstract class ImageResource implements HalResource {
    abstract String href();
    abstract int width();
    abstract int height();

    public static ImageResource create(String href, int width, int height) {
        return new AutoValue_ImageResource(href, width, height);
    }
}
