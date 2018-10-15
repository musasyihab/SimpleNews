package com.musasyihab.simplenews.di.module

import android.app.Application
import com.musasyihab.simplenews.BaseApp
import com.musasyihab.simplenews.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}