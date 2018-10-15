package com.musasyihab.simplenews.ui.news

import com.musasyihab.simplenews.model.GetNewsModel
import com.musasyihab.simplenews.ui.base.BaseContract

class NewsContract {

    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun showErrorMessage(error: String)
        fun loadDataSuccess(response: GetNewsModel)
    }

    interface Presenter: BaseContract.Presenter<NewsContract.View> {
        fun getNewsList(id: String, keyword: String)
    }
}