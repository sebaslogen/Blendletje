package com.sebaslogen.blendletje.data.remote.model;

import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

public class ArticleResource implements HalResource {
    @HalLink
    private String self;
    private String id;
    @HalEmbedded
    private ArticleManifestResource manifest;

    String self() {
        return self;
    }

    String id() {
        return id;
    }

    ArticleManifestResource manifest() {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArticleResource) {
            ArticleResource that = (ArticleResource) o;
            return (this.self.equals(that.self()))
                    && (this.id.equals(that.id()))
                    && (this.manifest.equals(that.manifest()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.self.hashCode();
        h *= 1000003;
        h ^= this.id.hashCode();
        h *= 1000003;
        h ^= this.manifest.hashCode();
        return h;
    }
}
