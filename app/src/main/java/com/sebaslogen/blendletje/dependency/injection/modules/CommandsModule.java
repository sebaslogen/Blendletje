package com.sebaslogen.blendletje.dependency.injection.modules;

import com.sebaslogen.blendletje.data.database.DatabaseManager;
import com.sebaslogen.blendletje.data.remote.ArticlesServer;
import com.sebaslogen.blendletje.domain.commands.RequestArticlesCommand;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CommandsModule {

    @Provides
    @Singleton
    RequestArticlesCommand.RequestArticlesCommandBuilder providesRequestArticlesCommandBuilder(
            final ArticlesServer articlesServer, final DatabaseManager databaseManager) {
        return new RequestArticlesCommand.RequestArticlesCommandBuilder(articlesServer, databaseManager);
    }
}
