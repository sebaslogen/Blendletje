package com.sebaslogen.blendletje.data.remote.model;

public class ArticleImagesContainer {
    private ArticleImagesLinks _links;
    private String caption;

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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleImagesContainer) {
            ArticleImagesContainer that = (ArticleImagesContainer) o;
            return (this._links.equals(that._links()))
                    && (this.caption.equals(that.caption()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this._links.hashCode();
        h *= 1000003;
        h ^= this.caption.hashCode();
        return h;
    }

}
