package com.sebaslogen.blendletje.ui.activities.recyclerview;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.exceptions.OnErrorNotImplementedException;

import static com.sebaslogen.blendletje.ui.utils.TextUtils.getMarkupStrippedString;

public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final short VIEW_TYPE_ARTICLE = 0;
    private static final short VIEW_TYPE_ADVERTISEMENT = 1;
    private final List<ListItem> mItemsList;
    private int mLastPosition = -1; // Remember the last item shown on screen for animations
    private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();

    /**
     * Constructor of an adapter for a list of items to bind and display in a recyclerView
     *
     * @param itemsList List of items
     */
    public ItemsListAdapter(final List<ListItem> itemsList) {
        mItemsList = itemsList;
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
        // TODO :Extract common interface with advertisement ViewHolder
        setAnimation(((ArticleItemViewHolder) holder).getContainer(), position);
    }

    private void setAnimation(final CardView view, final int position) {
        if (position > mLastPosition)
        {
            final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.card_appears);
            animation.setStartOffset(300);
            animation.setInterpolator(mDecelerateInterpolator);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }

    private void bindArticleItem(final Article article, final ArticleItemViewHolder holder) {
        holder.setTitle(article.contents().title());
        final List<ArticleImage> images = article.images();
        if (images.isEmpty()) {
            holder.clearImage();
        } else {
            final ArticleImage articleImage = images.get(0);
            final ImageMetadata image = articleImage.large();
            holder.setImage(image.url(), image.height(), articleImage.caption());
        }
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return (mItemsList.get(position) instanceof Article) ? VIEW_TYPE_ARTICLE : VIEW_TYPE_ADVERTISEMENT;
    }

    private class ArticleItemViewHolder extends RecyclerView.ViewHolder {

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
            final Context context = mImage.getContext();
            Picasso.with(context).cancelRequest(mImage);
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.empty)
                    .error(R.drawable.empty)
                    .into(mImage);
        }

        void clearImage() {
            Picasso.with(mImage.getContext()).cancelRequest(mImage);
            mImage.setLayoutParams(mDefaultLayoutParams);
            mImage.setContentDescription(mDefaultImageContentDescription);
            mImage.setImageDrawable(null);
        }

        CardView getContainer() {
            return mContainer;
        }
    }
}
