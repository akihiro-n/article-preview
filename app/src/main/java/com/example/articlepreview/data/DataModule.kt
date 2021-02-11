package com.example.articlepreview.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideArticleRepository(api: QiitaApi) = ArticleRepository(api)

    @Provides
    fun provideTagRepository(api: QiitaApi) = TagRepository(api)
}