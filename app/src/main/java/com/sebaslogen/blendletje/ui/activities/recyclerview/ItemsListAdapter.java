package com.sebaslogen.blendletje.ui.activities.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ArticleImage;
import com.sebaslogen.blendletje.domain.model.ImageMetadata;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;

import java.util.List;

import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Func4;

import static com.sebaslogen.blendletje.ui.utils.TextUtils.getMarkupStrippedString;

public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final short VIEW_TYPE_ARTICLE = 0;
    private static final short VIEW_TYPE_ADVERTISEMENT = 1;
    private List<ListItem> mItemsList;
    private final ImageLoader mImageLoader;
    private final Func4<View, String, String, String, Void> mItemClick;
    private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
    private int mLastPosition = -1; // Remember the last item shown on screen for animations

    /**
     * Constructor of an adapter for a list of items to bind and display in a recyclerView
     *
     * @param itemsList List of items
     */
    public ItemsListAdapter(final List<ListItem> itemsList, final ImageLoader imageLoader,
                            final Func4<View, String, String, String, Void> itemClick) {
        mItemsList = itemsList;
        mImageLoader = imageLoader;
        mItemClick = itemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view;
        switch (viewType) {
            case VIEW_TYPE_ARTICLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
                return new ArticleItemViewHolder(view);
            case VIEW_TYPE_ADVERTISEMENT:
            default:
                // TODO: Implement advertisement items
//                view = LayoutInflater.from(parent.getContext()).inflate(...
//                return new AdvertisementItemViewHolder(view);
                throw new OnErrorNotImplementedException(new Throwable("TODO: Implement advertisement items"));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int viewType = holder.getItemViewType();
        final ListItem listItem = mItemsList.get(position);
        switch (viewType) {
            case VIEW_TYPE_ARTICLE:
                bindArticleItem((Article) listItem, (ArticleItemViewHolder) holder);
                break;
            case VIEW_TYPE_ADVERTISEMENT:
                // TODO: Implement advertisement items biding
//                bindAdvertisementItem((Advertisement) listItem, (AdvertisementItemViewHolder) holder);
                throw new OnErrorNotImplementedException(new Throwable("TODO: Implement advertisement items biding"));
//                break;
            default:
                throw new IllegalArgumentException("View holder contains an unknown item type");
        }
        setAnimation(((ViewHolderAnimations) holder), position);
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((ViewHolderAnimations) holder).clearAnimation();
    }

    private void setAnimation(final ViewHolderAnimations view, final int position) {
        if (position > mLastPosition) {
            final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.card_appears);
            animation.setStartOffset(300);
            animation.setInterpolator(mDecelerateInterpolator);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }

    private void bindArticleItem(final Article article, final ArticleItemViewHolder holder) {
        final String title = article.contents().title();
        holder.setTitle(title);
        final List<ArticleImage> images = article.images();
        String imageUrl = null;
        if (images.isEmpty()) {
            holder.clearImage();
        } else {
            final ArticleImage articleImage = images.get(0);
            final ImageMetadata image = articleImage.large();
            imageUrl = image.url();
            holder.setImage(imageUrl, image.height(), articleImage.caption());
        }
        holder.setClickAction(mItemClick, article.id(), title, imageUrl);

    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return (mItemsList.get(position) instanceof Article) ? VIEW_TYPE_ARTICLE : VIEW_TYPE_ADVERTISEMENT;
    }

    public void overwriteList(final List<ListItem> newList) {
        mItemsList = newList;
        notifyDataSetChanged();
    }

    private interface ViewHolderAnimations {
        Context getContext();

        void startAnimation(Animation animation);

        void clearAnimation();
    }

    private class ArticleItemViewHolder extends RecyclerView.ViewHolder implements ViewHolderAnimations {

        private final CardView mContainer;
        private final TextView mTitle;
        private final ImageView mImage;
        private final String mDefaultImageContentDescription;
        private final FrameLayout.LayoutParams mDefaultLayoutParams;

        ArticleItemViewHolder(final View view) {
            super(view);
            mContainer = (CardView) view.findViewById(R.id.cv_item_container);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mImage = (ImageView) view.findViewById(R.id.iv_image);
            mDefaultImageContentDescription = view.getResources().getString(R.string.article_image_description);
            mDefaultLayoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        void setTitle(final String text) {
            mTitle.setText(getMarkupStrippedString(text));
        }

        void setImage(final String url, final int height, final String caption) {
            mImage.setContentDescription(caption);
            mImage.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    height));
            mImageLoader.cancelRequest(mImage);
            mImageLoader.load(url)
                    .placeholder(R.drawable.empty)
                    .error(R.drawable.empty)
                    .into(mImage);
        }

        void setClickAction(final Func4<View, String, String, String, Void> itemClick,
                            final String id, final String title, @Nullable final String imageUrl) {
            itemView.setOnClickListener(view -> itemClick.call(view, id, title, imageUrl));
        }

        void clearImage() {
            mImageLoader.cancelRequest(mImage);
            mImage.setLayoutParams(mDefaultLayoutParams);
            mImage.setContentDescription(mDefaultImageContentDescription);
            mImage.setImageDrawable(null);
        }

        @Override
        public Context getContext() {
            return mContainer.getContext();
        }

        @Override
        public void startAnimation(final Animation animation) {
            mContainer.startAnimation(animation);
        }

        @Override
        public void clearAnimation() {
            mContainer.clearAnimation();
        }
    }
}
