package com.sebaslogen.blendletje.data.remote;

import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;
import com.sebaslogen.blendletje.data.source.ArticlesDataSource;

import java.util.List;

public class ArticlesServer implements ArticlesDataSource {

    @Override
    public List<PopularArticlesResource> requestPopularArticles() {
        return null;
    }
}
