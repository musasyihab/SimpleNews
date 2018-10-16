package com.musasyihab.simplenews.ui.article

import io.reactivex.disposables.CompositeDisposable

class ArticlePresenter: ArticleContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: ArticleContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ArticleContract.View) {
        this.view = view
    }

}