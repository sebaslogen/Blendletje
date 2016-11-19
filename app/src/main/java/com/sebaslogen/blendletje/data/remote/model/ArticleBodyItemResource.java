package com.sebaslogen.blendletje.data.remote.model;

import com.google.auto.value.AutoValue;

import ch.halarious.core.HalResource;

@AutoValue
public abstract class ArticleBodyItemResource implements HalResource {
    abstract String type();
    abstract String content();

    public static ArticleBodyItemResource create(String type, String content) {
        return new AutoValue_ArticleBodyItemResource(type, content);
    }
}
