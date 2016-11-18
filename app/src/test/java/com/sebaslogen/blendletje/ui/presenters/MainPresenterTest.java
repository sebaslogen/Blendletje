package com.sebaslogen.blendletje.ui.presenters;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {

    private MainContract.ViewActions mViewActions;

    @Before
    public void setUp() throws Exception {
        mViewActions = mock(MainContract.ViewActions.class);
    }

    @Test
    public void onCreation_TitleIsShown() {
        // Given
        // When
        new MainPresenter(mViewActions);
        // Then
        verify(mViewActions).showTitle(anyString());
    }

}