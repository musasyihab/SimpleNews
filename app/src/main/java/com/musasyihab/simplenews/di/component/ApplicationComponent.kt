package com.musasyihab.simplenews.di.component

import com.musasyihab.simplenews.BaseApp
import com.musasyihab.simplenews.di.module.ApplicationModule
import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(application: BaseApp)

}