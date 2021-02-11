package com.example.articlepreview

import com.example.articlepreview.data.QiitaApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.commonmark.parser.Parser
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideMarkdownParser(): Parser =
        Parser.builder().build()

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideMoshiConvertorFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Singleton
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder().also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }.build()

    @Provides
    fun provideQiitaApi(
        httpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): QiitaApi = Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(moshiConverterFactory)
        .baseUrl("")
        .build()
        .create(QiitaApi::class.java)
}