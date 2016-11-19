package com.sebaslogen.blendletje.data.remote.model;

import java.util.ArrayList;
import java.util.List;

import ch.halarious.core.HalResource;

public class ArticleManifestResource implements HalResource {
    private List<ArticleBodyItemResource> body = new ArrayList<>();
    private List<ArticleImagesResource> images = new ArrayList<>();

    List<ArticleBodyItemResource> body() {
        return body;
    }

    List<ArticleImagesResource> images() {
        return images;
    }

    @Override
    public String toString() {
        return "ArticleManifestResource{"
                + "body=" + body + ", "
                + "images=" + images
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleManifestResource) {
            ArticleManifestResource that = (ArticleManifestResource) o;
            return (this.body.equals(that.body()))
                    && (this.images.equals(that.images()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.body.hashCode();
        h *= 1000003;
        h ^= this.images.hashCode();
        return h;
    }
}
