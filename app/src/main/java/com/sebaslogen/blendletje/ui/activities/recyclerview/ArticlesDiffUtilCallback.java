package com.sebaslogen.blendletje.ui.activities.recyclerview;


import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.List;

public class ArticlesDiffUtilCallback extends DiffUtil.Callback{

    private final List<ListItem> mOldArticles;
    private final List<ListItem> mNewArticles;

    public ArticlesDiffUtilCallback(final List<ListItem> newArticles, final List<ListItem> oldArticles) {
        mNewArticles = newArticles;
        mOldArticles = oldArticles;
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
        return mOldArticles.get(oldItemPosition).equals(mNewArticles.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return mOldArticles.get(oldItemPosition).equals(mNewArticles.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
