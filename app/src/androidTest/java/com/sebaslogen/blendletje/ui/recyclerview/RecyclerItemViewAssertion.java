package com.sebaslogen.blendletje.ui.recyclerview;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerItemViewAssertion<T> implements ViewAssertion {
    private final int position;
    private final T item;
    private final RecyclerViewInteraction.ItemViewAssertion<T> itemViewAssertion;

    public RecyclerItemViewAssertion(final int position, final T item,
                                     final RecyclerViewInteraction.ItemViewAssertion<T> itemViewAssertion) {
        this.position = position;
        this.item = item;
        this.itemViewAssertion = itemViewAssertion;
    }

    @Override public final void check(final View view, final NoMatchingViewException e) {
        final RecyclerView recyclerView = (RecyclerView) view;
        final RecyclerView.ViewHolder viewHolderForPosition =
                recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolderForPosition == null) {
            throw (new PerformException.Builder()).withActionDescription(toString())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(new IllegalStateException("No view holder at position: " + position))
                    .build();
        } else {
            final View viewAtPosition = viewHolderForPosition.itemView;
            itemViewAssertion.check(item, viewAtPosition, e);
        }
    }
}
