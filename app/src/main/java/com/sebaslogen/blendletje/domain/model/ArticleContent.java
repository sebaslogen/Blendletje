package com.sebaslogen.blendletje.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class ArticleContent {
    public abstract String title();
    public abstract List<String> paragraphs();

    public static ArticleContent create(final String title, final List<String> paragraphs) {
        return new AutoValue_ArticleContent(title, paragraphs);
    }
}
