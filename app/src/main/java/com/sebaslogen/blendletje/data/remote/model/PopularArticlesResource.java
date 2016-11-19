package com.sebaslogen.blendletje.data.remote.model;

import com.google.auto.value.AutoValue;

import java.util.List;

import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

@AutoValue
public abstract class PopularArticlesResource implements HalResource {
    @HalLink
    abstract String self();
    @HalEmbedded
    abstract List<ArticleResource> items();

    public static PopularArticlesResource create(String self, List<ArticleResource> items) {
        return new AutoValue_PopularArticlesResource(self, items);
    }
}
