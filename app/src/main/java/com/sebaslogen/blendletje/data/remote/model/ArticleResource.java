package com.sebaslogen.blendletje.data.remote.model;

import com.google.auto.value.AutoValue;

import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

@AutoValue
public abstract class ArticleResource implements HalResource {
    @HalLink
    abstract String self();
    abstract String id();
    @HalEmbedded
    abstract ArticleManifestResource manifest();

    public static ArticleResource create(String self, String id, ArticleManifestResource manifest) {
        return new AutoValue_ArticleResource(self, id, manifest);
    }
}
