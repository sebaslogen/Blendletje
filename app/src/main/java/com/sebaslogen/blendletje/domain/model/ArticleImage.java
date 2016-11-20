package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ArticleImage {
    abstract String caption();
    abstract MultipleSizeImage image();

    public static ArticleImage create(final String caption, final MultipleSizeImage image) {
        return new AutoValue_ArticleImage(caption, image);
    }
}
