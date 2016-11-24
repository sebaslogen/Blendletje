package com.sebaslogen.blendletje.ui.presenters;

public interface ArticleContract {

    interface ViewActions {

    }

    interface UserActions {

        void attachView();

        void deAttachView();
    }
}
