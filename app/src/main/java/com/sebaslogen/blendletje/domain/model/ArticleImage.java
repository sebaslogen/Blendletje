package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ArticleImage {
    public abstract String caption();
    public abstract ImageMetadata small();
    public abstract ImageMetadata medium();
    public abstract ImageMetadata large();

    public static ArticleImage create(final String caption, final ImageMetadata small,
                                      final ImageMetadata medium, final ImageMetadata large) {
        return new AutoValue_ArticleImage(caption, small, medium, large);
    }
}
