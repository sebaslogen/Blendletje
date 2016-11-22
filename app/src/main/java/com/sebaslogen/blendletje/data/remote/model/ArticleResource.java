package com.sebaslogen.blendletje.data.remote.model;

import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class ArticleResource implements HalResource, RealmModel {
    @HalLink
    private String self;
    private String id;
    @HalEmbedded
    private ArticleManifestResource manifest;

    public String self() {
        return self;
    }

    public String id() {
        return id;
    }

    public ArticleManifestResource manifest() {
        return manifest;
    }

    @Override
    public String toString() {
        return "ArticleResource{"
                + "self=" + self + ", "
                + "id=" + id + ", "
                + "manifest=" + manifest
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleResource) {
            final ArticleResource that = (ArticleResource) o;
            return (this.self.equals(that.self()))
                    && (this.id.equals(that.id()))
                    && (this.manifest.equals(that.manifest()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        if (this.self != null) {
            h *= 1000003;
            h ^= this.self.hashCode();
        }
        if (this.id != null) {
            h *= 1000003;
            h ^= this.id.hashCode();
        }
        if (this.manifest != null) {
            h *= 1000003;
            h ^= this.manifest.hashCode();
        }
        return h;
    }
}
