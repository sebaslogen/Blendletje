package com.sebaslogen.blendletje.domain.model;

public enum ContentType {
    KICKER,
    INTRO,
    SUBTITLE,
    SECOND_SUBTITLE,
    OWNER,
    PARAGRAPH,
    OTHER;

    public static final String KICKER_REPRESENTATION = "kicker";
    public static final String INTRO_REPRESENTATION = "intro";
    public static final String SUBTITLE_REPRESENTATION = "hl1";
    public static final String SECOND_SUBTITLE_REPRESENTATION = "hl2";
    public static final String OWNER_REPRESENTATION = "byline";
    public static final String PARAGRAPH_REPRESENTATION = "p";
}
