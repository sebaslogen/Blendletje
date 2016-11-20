package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Article implements ListItem {
    public abstract String id();
    public abstract List<ArticleContent> body();
    public abstract List<ArticleImage> images();

    public static Article create(final String id, final List<ArticleContent> body, final List<ArticleImage> images) {
        return new AutoValue_Article(id, body, images);
    }
}
