package com.sebaslogen.blendletje.data.remote.model;

import com.google.auto.value.AutoValue;

import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

@AutoValue
public abstract class ArticleImagesResource implements HalResource {
    @HalLink
    abstract ImageResource small();
    @HalLink
    abstract ImageResource medium();
    @HalLink
    abstract ImageResource large();
    abstract String caption();

    public static ArticleImagesResource create(ImageResource small, ImageResource medium, ImageResource large, String caption) {
        return new AutoValue_ArticleImagesResource(small, medium, large, caption);
    }
}
