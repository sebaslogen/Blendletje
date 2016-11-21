package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.domain.model.ListItem;

import java.util.List;

public interface MainContract {

    interface ViewActions {

        void showLoadingAnimation();

        void hideLoadingAnimation();

        void displayPopularArticlesList(List<ListItem> popularArticlesList);
    }

    interface UserActions {

        void attachView();

        void deAttachView();
    }
}
