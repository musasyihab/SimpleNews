package com.musasyihab.simplenews.ui.news

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.adapter.NewsListAdapter
import com.musasyihab.simplenews.di.component.DaggerActivityComponent
import com.musasyihab.simplenews.di.module.ActivityModule
import com.musasyihab.simplenews.model.ArticleModel
import com.musasyihab.simplenews.model.GetNewsModel
import com.musasyihab.simplenews.ui.article.ArticleActivity
import java.util.*
import javax.inject.Inject
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.musasyihab.simplenews.ui.view.ErrorPageView


class NewsActivity : AppCompatActivity(), NewsContract.View {

    object EXTRA {
        const val SELECTED_SOURCE_ID: String = "SELECTED_SOURCE_ID"
        const val SELECTED_SOURCE_NAME: String = "SELECTED_SOURCE_NAME"
    }

    @Inject
    lateinit var presenter: NewsContract.Presenter

    private lateinit var newsListView: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var actionBar: ActionBar
    private lateinit var searchContainer: LinearLayout
    private lateinit var searchInput: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var errorPage: ErrorPageView

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: NewsListAdapter
    private var selectedSourceId: String = ""
    private var selectedSourceName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        selectedSourceId = intent.getStringExtra(EXTRA.SELECTED_SOURCE_ID)
        selectedSourceName = intent.getStringExtra(EXTRA.SELECTED_SOURCE_NAME)

        initView()
        presenter.getNewsList(selectedSourceId)
    }

    private fun initView() {
        newsListView = findViewById(R.id.news_list) as RecyclerView
        loading = findViewById(R.id.news_loading) as ProgressBar
        searchContainer = findViewById(R.id.search_container) as LinearLayout
        searchInput = findViewById(R.id.search_input) as EditText
        searchIcon = findViewById(R.id.search_icon) as ImageView
        errorPage = findViewById(R.id.news_error_page) as ErrorPageView
        actionBar = supportActionBar!!

        actionBar.title = selectedSourceName

        linearLayoutManager = LinearLayoutManager(this)
        newsListView.layoutManager = linearLayoutManager

        adapter = NewsListAdapter(Collections.emptyList(), this, NewsListAdapterListener())
        newsListView.adapter = adapter

        searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboardAndSearch()
            }
            true
        }

        searchInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    newsListView.visibility = View.GONE
                    errorPage.visibility = View.GONE
                    presenter.getNewsList(selectedSourceId)
                }
            }

        })

        searchIcon.setOnClickListener { hideKeyboardAndSearch() }

        newsListView.visibility = View.GONE
        errorPage.visibility = View.GONE
    }

    private fun hideKeyboardAndSearch() {
        val imm = getSystemService(
                INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null)
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        newsListView.visibility = View.GONE
        showProgress(false)
        presenter.searchNews(selectedSourceId, searchInput.text.toString())
    }

    private fun loadToView(data: GetNewsModel) {
        if (!data.articles.isEmpty()) {
            newsListView.visibility = View.VISIBLE
            errorPage.visibility = View.GONE

            linearLayoutManager = LinearLayoutManager(this)
            newsListView.layoutManager = linearLayoutManager

            adapter = NewsListAdapter(data.articles, this, NewsListAdapterListener())
            newsListView.adapter = adapter
        } else {
            newsListView.visibility = View.GONE
            errorPage.visibility = View.VISIBLE
            errorPage.setErrorText(getString(R.string.empty_data))
            errorPage.setReloadBtnVisibility(false)
        }
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()

        activityComponent.inject(this)
    }

    override fun showProgress(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showErrorMessage(error: String) {
        Toast.makeText(this, "showErrorMessage: "+error, Toast.LENGTH_SHORT).show()
        newsListView.visibility = View.GONE
        errorPage.visibility = View.VISIBLE
        errorPage.setErrorText(getString(R.string.error_message))
        errorPage.setReloadBtnVisibility(true)
    }

    override fun loadDataSuccess(response: GetNewsModel) {
        loadToView(response)
    }

    private inner class NewsListAdapterListener : NewsListAdapter.Listener {

        override fun onItemClicked(item: ArticleModel, position: Int) {
            val articleIntent = Intent(this@NewsActivity, ArticleActivity::class.java)
            articleIntent.putExtra(ArticleActivity.EXTRA.SELECTED_ARTICLE_URL, item.url)
            articleIntent.putExtra(ArticleActivity.EXTRA.SELECTED_ARTICLE_TITLE, item.title)
            startActivity(articleIntent)
        }

    }
}
