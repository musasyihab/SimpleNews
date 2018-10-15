package com.musasyihab.simplenews.ui.news

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.adapter.NewsListAdapter
import com.musasyihab.simplenews.di.component.DaggerActivityComponent
import com.musasyihab.simplenews.di.module.ActivityModule
import com.musasyihab.simplenews.model.ArticleModel
import com.musasyihab.simplenews.model.GetNewsModel
import java.util.*
import javax.inject.Inject

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
        presenter.getNewsList(selectedSourceId, "")
    }

    private fun initView() {
        newsListView = findViewById(R.id.news_list) as RecyclerView
        loading = findViewById(R.id.news_loading) as ProgressBar
        actionBar = supportActionBar!!

        actionBar!!.title = selectedSourceName

        linearLayoutManager = LinearLayoutManager(this)
        newsListView!!.layoutManager = linearLayoutManager

        adapter = NewsListAdapter(Collections.emptyList(), this, NewsListAdapterListener())
        newsListView!!.adapter = adapter

        newsListView!!.visibility = View.GONE
    }

    private fun loadToView(data: GetNewsModel) {
        newsListView!!.visibility = View.VISIBLE
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
            Toast.makeText(this@NewsActivity, "${item.title} clicked!!", Toast.LENGTH_SHORT).show()
//            val detailIntent = Intent(this@MainActivity, IKDetailActivity::class.java)
//            val ijinKerjaId = item.getIjinKerja().getId()
//            detailIntent.putExtra(IKDetailActivity.IK_ID, ijinKerjaId)
//            startActivity(detailIntent)
        }

    }
}
