package com.sebaslogen.blendletje.domain.mappers;

import com.sebaslogen.blendletje.data.remote.model.ArticleBodyItemResource;
import com.sebaslogen.blendletje.data.remote.model.ArticleImagesContainer;
import com.sebaslogen.blendletje.data.remote.model.ArticleImagesLinks;
import com.sebaslogen.blendletje.data.remote.model.ArticleManifestResource;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.ImageResource;
import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ArticleContent;
import com.sebaslogen.blendletje.domain.model.ArticleImage;
import com.sebaslogen.blendletje.domain.model.ContentType;
import com.sebaslogen.blendletje.domain.model.ImageMetadata;
import com.sebaslogen.blendletje.domain.model.MultipleSizeImage;

import java.util.ArrayList;
import java.util.List;

public class ArticlesDataMapper {
    public static List<Article> convertPopularArticlesListToDomain(final PopularArticlesResource popularArticles) {
        final ArrayList<Article> articles = new ArrayList<>();
        final List<ArticleResource> articleResources = popularArticles.items();
        if (articleResources != null) {
            //noinspection Convert2streamapi
            for (final ArticleResource articleResource : articleResources) {
                articles.add(extractArticle(articleResource));
            }
        }
        return articles;
    }

    private static Article extractArticle(final ArticleResource articleResource) {
        final String id = articleResource.id();
        return Article.create(id, extractContents(articleResource), extractImages(articleResource));
    }

    private static List<ArticleImage> extractImages(final ArticleResource articleResource) {
        final ArrayList<ArticleImage> articleImages = new ArrayList<>();
        final ArticleManifestResource manifest = articleResource.manifest();
        if (manifest != null) {
            final List<ArticleImagesContainer> images = manifest.images();
            if (images != null) {
                for (final ArticleImagesContainer articleImagesContainer : images) {
                    final String caption = articleImagesContainer.caption();
                    try {
                        articleImages.add(ArticleImage.create(caption, extractImage(articleImagesContainer)));
                    } catch (final NullPointerException ignore) {
                    }
                }
            }
        }
        return articleImages;
    }

    private static MultipleSizeImage extractImage(final ArticleImagesContainer articleImagesContainer) {

        final ArticleImagesLinks articleImagesLinks = articleImagesContainer._links();
        if (articleImagesLinks != null) {
            return MultipleSizeImage.create(
                    extractImageMetadata(articleImagesLinks.small()),
                    extractImageMetadata(articleImagesLinks.medium()),
                    extractImageMetadata(articleImagesLinks.large()));
        }
        throw new NullPointerException();
    }

    private static ImageMetadata extractImageMetadata(final ImageResource imageResource) {
        return ImageMetadata.create(imageResource.href(), imageResource.width(), imageResource.height());
    }

    private static List<ArticleContent> extractContents(final ArticleResource articleResource) {
        final ArrayList<ArticleContent> articleContents = new ArrayList<>();
        final ArticleManifestResource manifest = articleResource.manifest();
        if (manifest != null) {
            final List<ArticleBodyItemResource> body = manifest.body();
            if (body != null) {
                for (final ArticleBodyItemResource articleBodyItemResource : body) {
                    articleContents.add(ArticleContent.create(
                            extractContentType(articleBodyItemResource.type()),
                            articleBodyItemResource.content()));
                }
            }
        }
        return articleContents;
    }

    private static ContentType extractContentType(final String type) {
        switch (type) {
            case ContentType.INTRO_REPRESENTATION:
                return ContentType.INTRO;
            case ContentType.KICKER_REPRESENTATION:
                return ContentType.KICKER;
            case ContentType.OWNER_REPRESENTATION:
                return ContentType.OWNER;
            case ContentType.PARAGRAPH_REPRESENTATION:
                return ContentType.PARAGRAPH;
            case ContentType.SUBTITLE_REPRESENTATION:
                return ContentType.SUBTITLE;
            case ContentType.SECOND_SUBTITLE_REPRESENTATION:
                return ContentType.SECOND_SUBTITLE;
            default:
                return ContentType.OTHER;
        }
    }
}
