package com.example.articlepreview

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        // ダークテーマを常にON！
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        super.onCreate()
    }
}