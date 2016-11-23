package com.sebaslogen.blendletje.data.remote.model;

import java.util.Objects;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ArticleImagesContainer implements RealmModel {
    private ArticleImagesLinks _links;
    private String caption;

    public ArticleImagesContainer() {
    }

    public ArticleImagesContainer(final ArticleImagesLinks _links, final String caption) {
        this._links = _links;
        this.caption = caption;
    }

    public ArticleImagesLinks _links() {
        return _links;
    }

    public String caption() {
        return caption;
    }

    @Override
    public String toString() {
        return "ArticleImagesLinks{"
                + "_links=" + _links + ", "
                + "caption=" + caption
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleImagesContainer) {
            final ArticleImagesContainer that = (ArticleImagesContainer) o;
            return (Objects.equals(_links, that._links()))
                    && (Objects.equals(caption, that.caption()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        if (this._links != null) {
            h *= 1000003;
            h ^= this._links.hashCode();
        }
        if (this.caption != null) {
            h *= 1000003;
            h ^= this.caption.hashCode();
        }
        return h;
    }

}
