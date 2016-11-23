package com.sebaslogen.blendletje.data.database;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Observable;
import rx.functions.Func0;
import rx.observers.TestSubscriber;
import utils.MockDataProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseManagerTest {

    private DatabaseManager mDatabaseManager;

    @Before
    public void setUp() throws Exception {
        final RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        final Func0<Realm> dbGetter = () -> Realm.getInstance(config);
        mDatabaseManager = new DatabaseManager(dbGetter, config);
    }

    @Test
    public void storeAndRetrieveArticleFromDatabase() throws Exception {
        // Given
        final ArticleResource article = MockDataProvider.provideMockedArticle();
        // When
        mDatabaseManager.storeObject(article);
        final Observable<ArticleResource> articleObservable = mDatabaseManager.requestArticle(article.id());
        final TestSubscriber<ArticleResource> testSubscriber = new TestSubscriber<>();
        articleObservable.subscribe(testSubscriber);

        // Then the request is correctly received
        final List<ArticleResource> events = testSubscriber.getOnNextEvents();
        testSubscriber.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final ArticleResource receivedArticle = events.get(0);
        assertEquals(article.id(), receivedArticle.id());
    }

}