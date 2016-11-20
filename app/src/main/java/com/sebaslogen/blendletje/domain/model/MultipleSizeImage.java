package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MultipleSizeImage {
    abstract ImageMetadata small();
    abstract ImageMetadata medium();
    abstract ImageMetadata large();

    public static MultipleSizeImage create(final ImageMetadata small, final ImageMetadata medium,
                                           final ImageMetadata large) {
        return new AutoValue_MultipleSizeImage(small, medium, large);
    }
}
