package utils;

import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ArticleContent;
import com.sebaslogen.blendletje.domain.model.ArticleImage;
import com.sebaslogen.blendletje.domain.model.ImageMetadata;
import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides data to consumers instead of using backend
 */
public class MockDataProvider {

    public static List<ListItem> provideMockedDomainListOfListItem() {
        final List<ListItem> listItems = new ArrayList<>();
        listItems.addAll(provideMockedDomainListOfArticles());
        return listItems;
    }

    public static List<Article> provideMockedDomainListOfArticles() {
        final List<Article> articles = new ArrayList<>();
        final ArrayList<ArticleImage> images = new ArrayList<>();
        images.add(ArticleImage.create("Raj Raghunathan op de Bloemenmarkt in Amsterdam.",
                ImageMetadata.create("https://static.blendle.nl/publication/vkn/2016/11/17/item/7352758/version/1/image/small/d0638c3c7bfe75fcb443e61abf2b5ad7b58a886c.jpg",
                300, 240),
                ImageMetadata.create("https://static.blendle.nl/publication/vkn/2016/11/17/item/7352758/version/1/image/medium/d0638c3c7bfe75fcb443e61abf2b5ad7b58a886c.jpg",
                        600, 480),
                ImageMetadata.create("https://static.blendle.nl/publication/vkn/2016/11/17/item/7352758/version/1/image/large/d0638c3c7bfe75fcb443e61abf2b5ad7b58a886c.jpg",
                        900, 720)));
        final ArrayList<String> paragraphs = new ArrayList<>();
        paragraphs.add("Als je zo slim bent, waarom ben je dan niet gelukkig? Het is een vraag die wellicht enige wrevel oproept, vanwege de mild goeroeëske ondertoon. Maar het blijkt vooral een vraag die op een gure vrijdagavond de gehele Lutherse Kerk op het Spui in Amsterdam weet te vullen met gelukzoekers.");
        paragraphs.add("Steller van de vraag is Raj Raghunathan, een Indiase hoogleraar van 49 die oogt als 35, zelf behoorlijk slim en al even gelukkig. Een combinatie…");
        articles.add(Article.create("bnl-vkn-20161117-7352758",
                ArticleContent.create("Geluk vind je ergens anders", paragraphs),
                images));
        return articles;
    }
}
