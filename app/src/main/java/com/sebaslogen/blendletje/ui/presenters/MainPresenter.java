package com.sebaslogen.blendletje.ui.presenters;

public class MainPresenter implements MainContract.UserActions {

    private final MainContract.ViewActions mViewActions;

    public MainPresenter(final MainContract.ViewActions viewActions) {
        mViewActions = viewActions;

        mViewActions.showTitle("Hola Blendle!");
    }
}
