package com.sebaslogen.blendletje.ui.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

public class TextUtils {

    @NonNull
    public static String getMarkupStrippedString(@NonNull final String text) {
        final String strippedText;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            strippedText = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            //noinspection deprecation
            strippedText = Html.fromHtml(text).toString();
        }
        return strippedText;
    }

    @NonNull
    public static Spanned getSpannedString(@NonNull final String text) {
        final Spanned strippedText;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            strippedText = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            //noinspection deprecation
            strippedText = Html.fromHtml(text);
        }
        return strippedText;
    }
}
