package com.sebaslogen.blendletje.data.remote.model;

import java.util.ArrayList;
import java.util.List;

import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;

public class PopularArticlesResource implements HalResource {
    @HalLink
    private String self;
    @HalEmbedded
    private List<ArticleResource> items = new ArrayList<>();

    public String self() {
        return self;
    }

    public List<ArticleResource> items() {
        return items;
    }

    public String toString() {
        return "PopularArticlesResource{"
                + "self=" + self + ", "
                + "items=" + items
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PopularArticlesResource) {
            PopularArticlesResource that = (PopularArticlesResource) o;
            return (this.self.equals(that.self()))
                    && (this.items.equals(that.items()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.self.hashCode();
        h *= 1000003;
        h ^= this.items.hashCode();
        return h;
    }
}
