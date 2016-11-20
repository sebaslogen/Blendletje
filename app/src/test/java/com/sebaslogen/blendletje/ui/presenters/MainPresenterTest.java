package com.sebaslogen.blendletje.ui.presenters;

import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;

import org.junit.Before;
import org.junit.Test;

import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {

    private MainContract.ViewActions mViewActions;

    @Before
    public void setUp() throws Exception {
        mViewActions = mock(MainContract.ViewActions.class);
    }

    @Test
    public void onCreation_TitleIsShown() {
        // Given there is a presenter
        final RequestArticlesCommand.RequestArticlesCommandBuilder requestCommandBuilder =
                new RequestArticlesCommand.RequestArticlesCommandBuilder(new ArticlesServer());
        // TODO: Test against mock, not production
        final MainPresenter presenter = spy(new MainPresenter(mViewActions, requestCommandBuilder));
        doReturn(Schedulers.immediate()).when(presenter).getUIScheduler();
        // When the view is attached
        presenter.attachView();
        // Then
        verify(mViewActions).showTitle(anyString());
    }

}