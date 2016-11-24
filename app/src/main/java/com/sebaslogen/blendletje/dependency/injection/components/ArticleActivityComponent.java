package com.sebaslogen.blendletje.dependency.injection.components;

import com.sebaslogen.blendletje.dependency.injection.modules.ArticleActivityModule;
import com.sebaslogen.blendletje.dependency.injection.modules.MainActivityModule;
import com.sebaslogen.blendletje.dependency.injection.scopes.ActivityScope;
import com.sebaslogen.blendletje.ui.activities.ArticleActivity;
import com.sebaslogen.blendletje.ui.activities.MainActivity;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = ArticleActivityModule.class
)
public interface ArticleActivityComponent {
    ArticleActivity inject(ArticleActivity activity);
}
