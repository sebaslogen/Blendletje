package com.sebaslogen.blendletje.ui.presenters;

public class MainPresenter implements MainContract.UserActions {

    private final MainContract.ViewActions mViewActions;

    public MainPresenter(MainContract.ViewActions mViewActions) {
        this.mViewActions = mViewActions;

        mViewActions.showTitle("Hola Blendle!");
    }
}
