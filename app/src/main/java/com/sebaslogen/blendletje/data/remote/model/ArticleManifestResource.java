package com.sebaslogen.blendletje.data.remote.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import ch.halarious.core.HalResource;

@AutoValue
public abstract class ArticleManifestResource implements HalResource {
    abstract List<ArticleBodyItemResource> body();
    abstract List<ArticleImagesResource> images();

    public static ArticleManifestResource create(List<ArticleBodyItemResource> body, List<ArticleImagesResource> images) {
        return new AutoValue_ArticleManifestResource(body, images);
    }
}
