package com.musasyihab.simplenews.ui.news

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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



class NewsActivity : AppCompatActivity(), NewsContract.View {

    object EXTRA {
        const val SELECTED_SOURCE_ID: String = "SELECTED_SOURCE_ID"
        const val SELECTED_SOURCE_NAME: String = "SELECTED_SOURCE_NAME"
    }

    @Inject
    lateinit var presenter: NewsContract.Presenter

    private var newsListView: RecyclerView? = null
    private var loading: ProgressBar? = null
    private var actionBar: ActionBar? = null
    private var searchContainer: LinearLayout? = null
    private var searchInput: EditText? = null
    private var searchIcon: ImageView? = null

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
        actionBar = supportActionBar!!

        actionBar!!.title = selectedSourceName

        linearLayoutManager = LinearLayoutManager(this)
        newsListView!!.layoutManager = linearLayoutManager

        adapter = NewsListAdapter(Collections.emptyList(), this, NewsListAdapterListener())
        newsListView!!.adapter = adapter

        searchInput!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboardAndSearch()
            }
            true
        }

        searchIcon!!.setOnClickListener { hideKeyboardAndSearch() }

        newsListView!!.visibility = View.GONE
    }

    private fun hideKeyboardAndSearch() {
        val imm = getSystemService(
                INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null && currentFocus != null)
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        newsListView!!.visibility = View.GONE
        showProgress(false)
        presenter.searchNews(selectedSourceId, searchInput!!.text.toString())
    }

    private fun loadToView(data: GetNewsModel) {
        newsListView!!.visibility = View.VISIBLE

        linearLayoutManager = LinearLayoutManager(this)
        newsListView!!.layoutManager = linearLayoutManager

        adapter = NewsListAdapter(data.articles, this, NewsListAdapterListener())
        newsListView!!.adapter = adapter
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

    override fun showErrorMessage(error: String) {
        Toast.makeText(this, "showErrorMessage: "+error, Toast.LENGTH_SHORT).show()
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
