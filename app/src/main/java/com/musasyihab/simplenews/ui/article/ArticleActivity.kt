package com.musasyihab.simplenews.ui.article

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.widget.LinearLayout
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.di.component.DaggerActivityComponent
import com.musasyihab.simplenews.di.module.ActivityModule
import javax.inject.Inject
import android.webkit.WebViewClient



class ArticleActivity : AppCompatActivity(), ArticleContract.View {

    object EXTRA {
        const val SELECTED_ARTICLE_URL: String = "SELECTED_ARTICLE_URL"
        const val SELECTED_ARTICLE_TITLE: String = "SELECTED_ARTICLE_TITLE"
    }

    @Inject
    lateinit var presenter: ArticleContract.Presenter

    private lateinit var webView: WebView
    private lateinit var loading: LinearLayout
    private lateinit var actionBar: ActionBar

    private var selectedArticleUrl: String = ""
    private var selectedArticleTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        selectedArticleUrl = intent.getStringExtra(EXTRA.SELECTED_ARTICLE_URL)
        selectedArticleTitle = intent.getStringExtra(EXTRA.SELECTED_ARTICLE_TITLE)

        initView()
    }

    private fun initView() {
        webView = findViewById<WebView>(R.id.webview)
        loading = findViewById<LinearLayout>(R.id.article_loading)
        actionBar = supportActionBar!!

        actionBar.title = selectedArticleTitle

        webView.loadUrl(selectedArticleUrl)

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                showProgress(false)
            }
        }

    }


    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()

        activityComponent.inject(this)
    }

    override fun showProgress(show: Boolean) {
        loading!!.visibility = if (show) View.VISIBLE else View.GONE
    }

}