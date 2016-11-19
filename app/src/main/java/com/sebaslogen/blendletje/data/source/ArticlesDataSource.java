package com.sebaslogen.blendletje.data.source;

import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import java.io.IOException;

import rx.Observable;

public interface ArticlesDataSource {

    PopularArticlesResource requestPopularArticles() throws IOException;

    Observable<PopularArticlesResource> requestPopularArticlesObservable(int amount, int page);
}
