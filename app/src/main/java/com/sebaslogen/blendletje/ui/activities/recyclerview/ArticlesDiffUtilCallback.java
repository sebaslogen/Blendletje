package com.sebaslogen.blendletje.ui.activities.recyclerview;


import android.support.v7.util.DiffUtil;

import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.List;

public class ArticlesDiffUtilCallback extends DiffUtil.Callback {

    private final List<ListItem> mOldArticles;
    private final List<ListItem> mNewArticles;

    public ArticlesDiffUtilCallback(final List<ListItem> oldArticles, final List<ListItem> newArticles) {
        mOldArticles = oldArticles;
        mNewArticles = newArticles;
    }

    @Override
    public int getOldListSize() {
        return mOldArticles.size();
    }

    @Override
    public int getNewListSize() {
        return mNewArticles.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        final ListItem oldItem = mOldArticles.get(oldItemPosition);
        final ListItem newItem = mNewArticles.get(newItemPosition);
        if (oldItem instanceof Article && newItem instanceof Article) {
            return ((Article) oldItem).id().equals(((Article) newItem).id());
        } else {
            return false;
        }
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return mOldArticles.get(oldItemPosition).equals(mNewArticles.get(newItemPosition));
    }

    // TODO: Use payload to notify changes instead of recreating
}