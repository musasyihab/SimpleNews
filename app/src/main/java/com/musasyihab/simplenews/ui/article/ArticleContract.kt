package com.musasyihab.simplenews.ui.article

import com.musasyihab.simplenews.ui.base.BaseContract

class ArticleContract {

    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
    }

    interface Presenter: BaseContract.Presenter<ArticleContract.View>
}