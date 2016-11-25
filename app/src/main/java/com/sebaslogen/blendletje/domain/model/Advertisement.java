package com.sebaslogen.blendletje.domain.model;

public class Advertisement implements ListItem {
    private final String mTitle;

    public Advertisement(final String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
