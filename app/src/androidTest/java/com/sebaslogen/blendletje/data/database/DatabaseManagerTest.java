package com.sebaslogen.blendletje.data.database;

import com.sebaslogen.blendletje.data.remote.model.ArticleResource;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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
        final Callable<Realm> dbGetter = () -> Realm.getInstance(config);
        mDatabaseManager = new DatabaseManager(dbGetter, config);
    }

    @Test
    public void storeAndRetrieveArticleFromDatabase() throws Exception {
        // Given
        final ArticleResource article = MockDataProvider.provideMockedArticle();
        // When
        mDatabaseManager.storeObject(article);
        final Single<ArticleResource> articleObservable = mDatabaseManager.requestArticle(article.id());
        final TestObserver<ArticleResource> testObserver = new TestObserver<>();
        articleObservable.subscribe(testObserver);

        // Then the request is correctly received
        testObserver.await(2, TimeUnit.SECONDS);
        final List<Object> events = testObserver.getEvents().get(0);
        testObserver.assertNoErrors();
        assertTrue("There should only one event with the request results", events.size() == 1);
        final ArticleResource receivedArticle = (ArticleResource) events.get(0);
        assertEquals(article.id(), receivedArticle.id());
    }

}