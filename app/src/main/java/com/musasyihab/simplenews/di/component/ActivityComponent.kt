package com.musasyihab.simplenews.di.component

import com.musasyihab.simplenews.di.module.ActivityModule
import com.musasyihab.simplenews.ui.article.ArticleActivity
import com.musasyihab.simplenews.ui.main.MainActivity
import com.musasyihab.simplenews.ui.news.NewsActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(newsActivity: NewsActivity)
    fun inject(articleActivity: ArticleActivity)

}