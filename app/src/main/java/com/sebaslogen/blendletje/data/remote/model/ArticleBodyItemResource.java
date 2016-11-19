package com.sebaslogen.blendletje.data.remote.model;

public class ArticleBodyItemResource {
    private String type;
    private String content;

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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleBodyItemResource) {
            ArticleBodyItemResource that = (ArticleBodyItemResource) o;
            return (this.type.equals(that.type()))
                    && (this.content.equals(that.content()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.type.hashCode();
        h *= 1000003;
        h ^= this.content.hashCode();
        return h;
    }
}
