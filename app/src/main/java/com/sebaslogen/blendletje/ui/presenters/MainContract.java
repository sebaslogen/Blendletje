package com.sebaslogen.blendletje.ui.presenters;

public interface MainContract {

    interface ViewActions {

        void showTitle(String text);
    }

    interface UserActions {

        void attachView();

        void deAttachView();
    }
}
