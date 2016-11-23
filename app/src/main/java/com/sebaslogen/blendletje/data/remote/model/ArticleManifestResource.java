package com.sebaslogen.blendletje.data.remote.model;

import java.util.List;
import java.util.Objects;

import ch.halarious.core.HalResource;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ArticleManifestResource implements HalResource, RealmModel {
    private String id;
    private RealmList<ArticleBodyItemResource> body = new RealmList<>();
    private RealmList<ArticleImagesContainer> images = new RealmList<>();

    public ArticleManifestResource() {
    }

    public ArticleManifestResource(final String id, final RealmList<ArticleBodyItemResource> body,
                                   final RealmList<ArticleImagesContainer> images) {
        this.id = id;
        this.body = body;
        this.images = images;
    }

    public String id() {
        return id;
    }

    public List<ArticleBodyItemResource> body() {
        return body;
    }

    public List<ArticleImagesContainer> images() {
        return images;
    }

    @Override
    public String toString() {
        return "ArticleManifestResource{"
                + "id=" + id + ", "
                + "contents=" + body + ", "
                + "images=" + images
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleManifestResource) {
            final ArticleManifestResource that = (ArticleManifestResource) o;
            return (Objects.equals(id, that.id()))
                    && (Objects.equals(body, that.body()))
                    && (Objects.equals(images, that.images()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        if (this.id != null) {
            h *= 1000003;
            h ^= this.id.hashCode();
        }
        h *= 1000003;
        h ^= this.body.hashCode();
        h *= 1000003;
        h ^= this.images.hashCode();
        return h;
    }
}
