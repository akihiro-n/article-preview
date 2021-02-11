package com.example.articlepreview.data.module

import com.example.articlepreview.data.ArticleRepository
import com.example.articlepreview.data.QiitaApi
import com.example.articlepreview.data.TagRepository
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