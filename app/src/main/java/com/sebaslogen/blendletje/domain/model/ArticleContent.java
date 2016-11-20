package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ArticleContent {
    public abstract ContentType type();
    public abstract String content();

    public static ArticleContent create(final ContentType type, final String content) {
        return new AutoValue_ArticleContent(type, content);
    }
}
