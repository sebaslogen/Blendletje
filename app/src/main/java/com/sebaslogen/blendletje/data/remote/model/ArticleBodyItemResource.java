package com.sebaslogen.blendletje.data.remote.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ArticleBodyItemResource implements RealmModel {
    private String type;
    private String content;

    public ArticleBodyItemResource() {
    }

    public ArticleBodyItemResource(final String type, final String content) {
        this.type = type;
        this.content = content;
    }

    public String type() {
        return type;
    }

    public String content() {
        return content;
    }

    @Override
    public String toString() {
        return "ArticleBodyItemResource{"
                + "type=" + type + ", "
                + "content=" + content
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleBodyItemResource) {
            final ArticleBodyItemResource that = (ArticleBodyItemResource) o;
            return (this.type.equals(that.type()))
                    && (this.content.equals(that.content()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        if (this.type != null) {
            h *= 1000003;
            h ^= this.type.hashCode();
        }
        if (this.content != null) {
            h *= 1000003;
            h ^= this.content.hashCode();
        }
        return h;
    }
}
