package com.sebaslogen.blendletje.data.remote.model;

import java.util.List;

import ch.halarious.core.HalEmbedded;
import ch.halarious.core.HalLink;
import ch.halarious.core.HalResource;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class PopularArticlesResource implements HalResource, RealmModel {
    @HalLink
    private String self;
    @HalLink
    private String prev;
    @HalLink
    private String next;
    @HalEmbedded
    private RealmList<ArticleResource> items = new RealmList<>();

    public PopularArticlesResource() {
    }

    public PopularArticlesResource(final String self, final String prev, final String next,
                                   final RealmList<ArticleResource> items) {
        this.self = self;
        this.prev = prev;
        this.next = next;
        this.items = items;
    }

    public String self() {
        return self;
    }

    public String prev() {
        return prev;
    }
    public String next() {
        return next;
    }

    public List<ArticleResource> items() {
        return items;
    }

    public String toString() {
        return "PopularArticlesResource{"
                + "self=" + self + ", "
                + "self=" + prev + ", "
                + "self=" + next + ", "
                + "items=" + items
                + "}";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PopularArticlesResource) {
            final PopularArticlesResource that = (PopularArticlesResource) o;
            return (this.self.equals(that.self()))
                    && (this.prev.equals(that.prev()))
                    && (this.next.equals(that.next()))
                    && (this.items.equals(that.items()));
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
        if (this.prev != null) {
            h *= 1000003;
            h ^= this.prev.hashCode();
        }
        if (this.next != null) {
            h *= 1000003;
            h ^= this.next.hashCode();
        }
        h *= 1000003;
        h ^= this.items.hashCode();
        return h;
    }
}
