package com.sebaslogen.blendletje.ui.activities.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.util.DiffUtil;
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

import com.github.florent37.picassopalette.PicassoPalette;
import com.sebaslogen.blendletje.R;
import com.sebaslogen.blendletje.domain.model.Advertisement;
import com.sebaslogen.blendletje.domain.model.Article;
import com.sebaslogen.blendletje.domain.model.ArticleImage;
import com.sebaslogen.blendletje.domain.model.ImageMetadata;
import com.sebaslogen.blendletje.domain.model.ListItem;
import com.sebaslogen.blendletje.ui.utils.ImageLoader;

import java.util.List;

import io.reactivex.functions.Function4;
import timber.log.Timber;

import static com.sebaslogen.blendletje.ui.utils.TextUtils.getMarkupStrippedString;

public class ItemsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final short VIEW_TYPE_ARTICLE = 0;
    private static final short VIEW_TYPE_ADVERTISEMENT = 1;
    private final ImageLoader mImageLoader;
    private final Function4<View, String, String, String, Void> mItemClick;
    private final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
    private List<ListItem> mItemsList;
    private int mLastPosition = -1; // Remember the last item shown on screen for animations

    /**
     * Constructor of an adapter for a list of items to bind and display in a recyclerView
     *
     * @param itemsList List of items
     */
    public ItemsListAdapter(final List<ListItem> itemsList, final ImageLoader imageLoader,
                            final Function4<View, String, String, String, Void> itemClick) {
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
                return new ArticleItemViewHolder(view, mImageLoader);
            case VIEW_TYPE_ADVERTISEMENT:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertisement_item, parent, false);
                return new AdvertisementItemViewHolder(view, mImageLoader);
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
                bindAdvertisementItem((Advertisement) listItem, position, (AdvertisementItemViewHolder) holder);
                break;
            default:
                throw new IllegalArgumentException("View holder contains an unknown item type");
        }
        setAnimation(((ViewHolderAnimations) holder), position);
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        ((ViewHolderAnimations) holder).clearAnimation();
    }

    /**
     * This method will animate the appearance of new items in the recycler view
     *
     * @param view     View item to animate
     * @param position Position of the item in the list to make sure only new items are animated
     */
    private void setAnimation(final ViewHolderAnimations view, final int position) {
        if (position > mLastPosition) {
            final Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.card_appears);
            animation.setStartOffset(300);
            animation.setInterpolator(mDecelerateInterpolator);
            view.startAnimation(animation);
            mLastPosition = position;
        }
    }

    private void bindAdvertisementItem(final Advertisement advertisement, final int position,
                                       final AdvertisementItemViewHolder holder) {
        final String title = advertisement.getTitle();
        holder.setTitle(title);
        final String imageUrl = "http://lorempixel.com/600/400/sports/" + (position % 10);
        holder.setImage(imageUrl);
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

    public void updateList(final List<ListItem> newList) {
        // TODO: calculate diff in separate thread for very long lists of items
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ArticlesDiffUtilCallback(mItemsList, newList));
        mItemsList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    private interface ViewHolderAnimations {
        Context getContext();

        void startAnimation(Animation animation);

        void clearAnimation();
    }

    private static class ArticleItemViewHolder extends RecyclerView.ViewHolder implements ViewHolderAnimations {

        private final CardView mContainer;
        private final TextView mTitle;
        private final ImageView mImageView;
        private final String mDefaultImageContentDescription;
        private final FrameLayout.LayoutParams mDefaultLayoutParams;
        private final ImageLoader mImageLoader;

        ArticleItemViewHolder(final View view, final ImageLoader imageLoader) {
            super(view);
            mContainer = (CardView) view.findViewById(R.id.cv_item_container);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mImageView = (ImageView) view.findViewById(R.id.iv_image);
            mDefaultImageContentDescription = view.getResources().getString(R.string.article_image_description);
            mImageLoader = imageLoader;
            mDefaultLayoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        void setTitle(final String text) {
            mTitle.setText(getMarkupStrippedString(text));
        }

        void setImage(final String imageUrl, final int height, final String caption) {
            mImageView.setContentDescription(caption);
            mImageView.setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                height)); // Prepare container size so it's already correct before image is loaded
            mImageLoader.cancelRequest(mImageView);
            mImageLoader.load(imageUrl)
                .placeholder(R.drawable.empty)
                .error(R.drawable.empty)
                .into(mImageView, PicassoPalette.with(imageUrl, mImageView)
                    .use(PicassoPalette.Profile.MUTED_DARK)
                    .intoCallBack(palette -> {
                        final Palette.Swatch swatch = palette.getDarkMutedSwatch();
                        if (swatch != null) {
                            mTitle.setBackgroundColor(
                                ColorUtils.setAlphaComponent(swatch.getRgb(), 200));
                        }
                    }));
        }

        void setClickAction(final Function4<View, String, String, String, Void> itemClick,
                            final String id, final String title, @Nullable final String imageUrl) {
            itemView.setOnClickListener(view -> {
                try {
                    itemClick.apply(view, id, title, imageUrl);
                } catch (final Exception e) {
                    Timber.e(e);
                }
            });
        }

        void clearImage() {
            mImageLoader.cancelRequest(mImageView);
            mImageView.setLayoutParams(mDefaultLayoutParams);
            mImageView.setContentDescription(mDefaultImageContentDescription);
            mImageView.setImageDrawable(null);
            mTitle.setBackgroundResource(R.color.article_title_background);
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

    private static class AdvertisementItemViewHolder extends RecyclerView.ViewHolder implements ViewHolderAnimations {

        private final CardView mContainer;
        private final TextView mTitle;
        private final ImageView mImageView;
        private final ImageLoader mImageLoader;

        AdvertisementItemViewHolder(final View view, final ImageLoader imageLoader) {
            super(view);
            mContainer = (CardView) view.findViewById(R.id.cv_ad_item_container);
            mTitle = (TextView) view.findViewById(R.id.tv_ad_title);
            mImageView = (ImageView) view.findViewById(R.id.iv_ad_image);
            mImageLoader = imageLoader;
        }

        void setTitle(final String text) {
            mTitle.setText(getMarkupStrippedString(text));
        }

        void setImage(final String imageUrl) {
            mImageLoader.cancelRequest(mImageView);
            mImageLoader.load(imageUrl)
                .placeholder(R.drawable.empty)
                .error(R.drawable.empty)
                .into(mImageView, PicassoPalette.with(imageUrl, mImageView)
                    .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                    .intoTextColor(mTitle, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                    .use(PicassoPalette.Profile.MUTED_DARK)
                    .intoCallBack(palette -> {
                        final Palette.Swatch swatch = palette.getDarkMutedSwatch();
                        if (swatch != null) {
                            mTitle.setBackgroundColor(
                                ColorUtils.setAlphaComponent(swatch.getRgb(), 200));
                        }
                    }));
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
