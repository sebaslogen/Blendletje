package com.sebaslogen.blendletje.data.remote.model;

import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

public class ArticleImagesResource implements HalResource {
    @HalLink
    private ImageResource small;
    @HalLink
    private ImageResource medium;
    @HalLink
    private ImageResource large;
    private String caption;

    ImageResource small() {
        return small;
    }

    ImageResource medium() {
        return medium;
    }

    ImageResource large() {
        return large;
    }

    String caption() {
        return caption;
    }

    @Override
    public String toString() {
        return "ArticleImagesResource{"
                + "small=" + small + ", "
                + "medium=" + medium + ", "
                + "large=" + large + ", "
                + "caption=" + caption
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleImagesResource) {
            ArticleImagesResource that = (ArticleImagesResource) o;
            return (this.small.equals(that.small()))
                    && (this.medium.equals(that.medium()))
                    && (this.large.equals(that.large()))
                    && (this.caption.equals(that.caption()));
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
        h *= 1000003;
        h ^= this.caption.hashCode();
        return h;
    }
}
