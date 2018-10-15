package com.musasyihab.simplenews.ui.main

import com.musasyihab.simplenews.model.GetSourcesModel
import com.musasyihab.simplenews.ui.base.BaseContract

class MainContract {

    interface View: BaseContract.View {
        fun showProgress(show: Boolean)
        fun showErrorMessage(error: String)
        fun loadDataSuccess(response: GetSourcesModel)
    }

    interface Presenter: BaseContract.Presenter<MainContract.View> {
        fun getSourceList()
    }
}