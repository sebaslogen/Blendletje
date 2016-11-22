package com.sebaslogen.blendletje.data.remote.model;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ImageResource implements RealmModel {
    private String href;
    private int width;
    private int height;

    public String href() {
        return href;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public String toString() {
        return "ImageResource{"
                + "href=" + href + ", "
                + "width=" + width + ", "
                + "height=" + height
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ImageResource) {
            final ImageResource that = (ImageResource) o;
            return (this.href.equals(that.href()))
                    && (this.width == that.width())
                    && (this.height == that.height());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        if (this.href != null) {
            h *= 1000003;
            h ^= this.href.hashCode();
        }
        h *= 1000003;
        h ^= this.width;
        h *= 1000003;
        h ^= this.height;
        return h;
    }
}
