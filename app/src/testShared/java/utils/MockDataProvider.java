package utils;

import com.sebaslogen.blendletje.data.remote.model.ArticleBodyItemResource;
import com.sebaslogen.blendletje.data.remote.model.ArticleImagesContainer;
import com.sebaslogen.blendletje.data.remote.model.ArticleImagesLinks;
import com.sebaslogen.blendletje.data.remote.model.ArticleManifestResource;
import com.sebaslogen.blendletje.data.remote.model.ArticleResource;
import com.sebaslogen.blendletje.data.remote.model.ImageResource;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ArticleContent;
import com.sebaslogen.blendletje.domain.model.ArticleImage;
import com.sebaslogen.blendletje.domain.model.ImageMetadata;
import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

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

    public static ArticleResource provideMockedArticle() {
        final String id = "bnl-vkn-20161117-7352758";
        final RealmList<ArticleBodyItemResource> body = new RealmList<>();
        body.add(new ArticleBodyItemResource("kicker", "Leven - INTERVIEW: RAJ RAGHUNATHAN"));
        body.add(new ArticleBodyItemResource("intro", "Op zoek naar geluk? Vrienden en familie zijn belangrijker dan succes in je werk, zegt geluksprofessor Raj Raghunathan."));
        body.add(new ArticleBodyItemResource("hl1", "Geluk vind je ergens anders"));
        body.add(new ArticleBodyItemResource("byline", "DOOR IANTHE SAHADAT"));
        body.add(new ArticleBodyItemResource("p", "Als je zo slim bent, waarom ben je dan niet gelukkig? Het is een vraag die wellicht enige wrevel oproept, vanwege de mild goeroeëske ondertoon. Maar het blijkt vooral een vraag die op een gure vrijdagavond de gehele Lutherse Kerk op het Spui in Amsterdam weet te vullen met gelukzoekers."));
        body.add(new ArticleBodyItemResource("p", "Steller van de vraag is Raj Raghunathan, een Indiase hoogleraar van 49 die oogt als 35, zelf behoorlijk slim en al even gelukkig. Een combinatie…"));
        final RealmList<ArticleImagesContainer> images = new RealmList<>();
        final ArticleImagesLinks links = new ArticleImagesLinks(
                new ImageResource("https://static.blendle.nl/publication/vkn/2016/11/17/item/7352758/version/1/image/small/d0638c3c7bfe75fcb443e61abf2b5ad7b58a886c.jpg",
                        300, 240),
                new ImageResource("https://static.blendle.nl/publication/vkn/2016/11/17/item/7352758/version/1/image/medium/d0638c3c7bfe75fcb443e61abf2b5ad7b58a886c.jpg",
                        600, 480),
                new ImageResource("https://static.blendle.nl/publication/vkn/2016/11/17/item/7352758/version/1/image/large/d0638c3c7bfe75fcb443e61abf2b5ad7b58a886c.jpg",
                        900, 720)
                );
        images.add(new ArticleImagesContainer(links, "Raj Raghunathan op de Bloemenmarkt in Amsterdam."));
        final ArticleManifestResource manifest = new ArticleManifestResource(id, body, images);
        return new ArticleResource(
                "https://ws.blendle.com/item/bnl-vkn-20161117-7352758", id,
                manifest);
    }
}
