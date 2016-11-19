package com.sebaslogen.blendletje.data.remote.model;

public class ArticleImagesLinks {
    private ImageResource small;
    private ImageResource medium;
    private ImageResource large;

    public ImageResource small() {
        return small;
    }

    public ImageResource medium() {
        return medium;
    }

    public ImageResource large() {
        return large;
    }

    @Override
    public String toString() {
        return "ArticleImagesLinks{"
                + "small=" + small + ", "
                + "medium=" + medium + ", "
                + "large=" + large + ", "
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleImagesLinks) {
            ArticleImagesLinks that = (ArticleImagesLinks) o;
            return (this.small.equals(that.small()))
                    && (this.medium.equals(that.medium()))
                    && (this.large.equals(that.large()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.small.hashCode();
        h *= 1000003;
        h ^= this.medium.hashCode();
        h *= 1000003;
        h ^= this.large.hashCode();
        return h;
    }
}
