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

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

/**
 * Class in charge of transforming data received from the data layer (backend or database)
 * into data model suitable for this application's domain and UI
 */
public class ArticlesDataMapper {

    /**
     * Map a container of popular articles from the data layer into a list of articles
     * in the domain layer
     *
     * @param popularArticles Object containing popular articles from the data layer
     * @return A new list of articles with only the required information for this application's domain and UI
     */
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
                    final ArticleImagesLinks articleImagesLinks = articleImagesContainer._links();
                    if ((caption != null) && (articleImagesLinks != null)) {
                        articleImages.add(ArticleImage.create(caption,
                                extractImageMetadata(articleImagesLinks.small()),
                                extractImageMetadata(articleImagesLinks.medium()),
                                extractImageMetadata(articleImagesLinks.large())));
                    }
                }
            }
        }
        return articleImages;
    }

    private static ImageMetadata extractImageMetadata(final ImageResource imageResource) {
        return ImageMetadata.create(imageResource.href(), imageResource.width(), imageResource.height());
    }

    private static ArticleContent extractContents(final ArticleResource articleResource) {
        String title = null;
        final List<String> paragraphs = new ArrayList<>();
        final ArticleManifestResource manifest = articleResource.manifest();
        if (manifest != null) {
            final List<ArticleBodyItemResource> body = manifest.body();
            if (body != null) {
                for (final ArticleBodyItemResource articleBodyItemResource : body) {
                    final String type = articleBodyItemResource.type();
                    if (ContentType.TITLE_REPRESENTATION.equalsIgnoreCase(type)) {
                        title = articleBodyItemResource.content();
                    } else if (ContentType.PARAGRAPH_REPRESENTATION.equalsIgnoreCase(type)) {
                        paragraphs.add(articleBodyItemResource.content());
                    }
                }
            }
        }
        if (title == null) {
            throw new MissingResourceException("No title text found when parsing data",
                    ArticlesDataMapper.class.getName(), "Manifest.intro");
        }
        return ArticleContent.create(title, paragraphs);
    }
}
