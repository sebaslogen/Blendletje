package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.domain.model.Advertisement;
import com.sebaslogen.blendletje.domain.model.Article;

public interface ArticleContract {

    interface ViewActions {

        void showLoadingAnimation();

        void hideLoadingAnimation();

        void displayArticle(Article article);

        void displayAdvertisement(Advertisement advertisement);
    }

    interface UserActions {

        void attachView();

        void deAttachView();
    }
}
