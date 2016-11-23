package com.sebaslogen.blendletje.ui.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Class in charge of loading images into Android view containers using Picasso
 * This is provided as a dependency by Dagger
 * Load method can be overwritten by tests to load local resources without hitting real network
 */
public class ImageLoader {

    private final Picasso mImageLoaderLib;

    public ImageLoader(final Context context) {
        mImageLoaderLib = Picasso.with(context);
    }


    public void cancelRequest(final ImageView imageView) {
        mImageLoaderLib.cancelRequest(imageView);
    }

    public RequestCreator load(final String url) {
        return mImageLoaderLib.load(url);
    }
}
