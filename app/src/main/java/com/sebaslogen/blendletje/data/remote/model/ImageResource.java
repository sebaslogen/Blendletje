package com.sebaslogen.blendletje.data.remote.model;

public class ImageResource {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ImageResource) {
            ImageResource that = (ImageResource) o;
            return (this.href.equals(that.href()))
                    && (this.width == that.width())
                    && (this.height == that.height());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.href.hashCode();
        h *= 1000003;
        h ^= this.width;
        h *= 1000003;
        h ^= this.height;
        return h;
    }
}
