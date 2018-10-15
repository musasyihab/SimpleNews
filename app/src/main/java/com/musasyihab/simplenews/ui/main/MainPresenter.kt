package com.musasyihab.simplenews.ui.main

import com.musasyihab.simplenews.api.ApiServiceInterface
import com.musasyihab.simplenews.model.GetSourcesModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter: MainContract.Presenter {
    private val subscriptions = CompositeDisposable()
    var api: ApiServiceInterface = ApiServiceInterface.create()
    private lateinit var view: MainContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MainContract.View) {
        this.view = view
    }

    override fun getSourceList() {
        view.showProgress(true)
        val obs = api.getSourceList()
        var subscribe = obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: GetSourcesModel? ->
                    view.loadDataSuccess(response!!)
                    view.showProgress(false)
                }, { error ->
                    view.showErrorMessage(error.localizedMessage)
                    view.showProgress(false)
                })

        subscriptions.add(subscribe)
    }
}