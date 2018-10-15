package com.musasyihab.simplenews.di.component

import com.musasyihab.simplenews.di.module.ActivityModule
import com.musasyihab.simplenews.ui.main.MainActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

}