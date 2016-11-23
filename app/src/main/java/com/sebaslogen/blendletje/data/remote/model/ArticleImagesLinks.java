package com.sebaslogen.blendletje.data.remote.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ArticleImagesLinks implements RealmModel {
    private ImageResource small;
    private ImageResource medium;
    private ImageResource large;

    public ArticleImagesLinks() {
    }

    public ArticleImagesLinks(final ImageResource small, final ImageResource medium,
                              final ImageResource large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

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
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleImagesLinks) {
            final ArticleImagesLinks that = (ArticleImagesLinks) o;
            return (this.small.equals(that.small()))
                    && (this.medium.equals(that.medium()))
                    && (this.large.equals(that.large()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        if (this.small != null) {
            h *= 1000003;
            h ^= this.small.hashCode();
        }
        if (this.medium != null) {
            h *= 1000003;
            h ^= this.medium.hashCode();
        }
        if (this.large != null) {
            h *= 1000003;
            h ^= this.large.hashCode();
        }
        return h;
    }
}
