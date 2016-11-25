package com.sebaslogen.blendletje.ui.pages;

import android.support.test.espresso.Espresso;

import com.sebaslogen.blendletje.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * This object represents the graphical user interface for the article's screen (a.k.a. page)
 * <p>
 * Methods in this class abstract the actions a user can perform on this page
 * the flows the user can follow to other pages
 * and also the checks that can be done on this page
 */
public class ArticlePage {

    /**
     * The constructor checks the basic features of any instance of this page:
     * main layout, toolbar and title
     */
    public ArticlePage() {
        onView(withId(R.id.cl_activity_article)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_title)).check(matches(isDisplayed()));
    }

    public void checkTitleIs(final String title) {
        onView(allOf(withId(R.id.tv_title), withText(title))).check(matches(isDisplayed()));
    }

    public MainPage pressBack() {
        Espresso.pressBack();
        return new MainPage();
    }
}
