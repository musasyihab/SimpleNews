package com.musasyihab.simplenews.ui.news

import com.musasyihab.simplenews.api.ApiServiceInterface
import com.musasyihab.simplenews.model.GetNewsModel
import com.musasyihab.simplenews.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsPresenter: NewsContract.Presenter {
    private val subscriptions = CompositeDisposable()
    var api: ApiServiceInterface = ApiServiceInterface.create()
    private lateinit var view: NewsContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: NewsContract.View) {
        this.view = view
    }

    override fun getNewsList(id: String) {
        view.showProgress(true)
        val obs = api.getNewsList(id, Constants.PAGE_SIZE, "")
        var subscribe = obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: GetNewsModel? ->
                    view.loadDataSuccess(response!!)
                    view.showProgress(false)
                }, { error ->
                    view.showErrorMessage(error.localizedMessage)
                    view.showProgress(false)
                })

        subscriptions.add(subscribe)
    }

    override fun searchNews(id: String, keyword: String) {
        view.showProgress(true)
        val obs = api.getNewsList(id, Constants.PAGE_SIZE, keyword)
        var subscribe = obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: GetNewsModel? ->
                    view.loadDataSuccess(response!!)
                    view.showProgress(false)
                }, { error ->
                    view.showErrorMessage(error.localizedMessage)
                    view.showProgress(false)
                })

        subscriptions.add(subscribe)
    }

}