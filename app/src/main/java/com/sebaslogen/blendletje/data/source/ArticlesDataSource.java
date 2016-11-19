package com.sebaslogen.blendletje.data.source;

import com.sebaslogen.blendletje.data.remote.model.PopularArticlesResource;

import java.util.List;

public interface ArticlesDataSource {
    List<PopularArticlesResource> requestPopularArticles();
}
