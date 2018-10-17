package com.musasyihab.simplenews.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.adapter.SourceListAdapter
import com.musasyihab.simplenews.di.component.DaggerActivityComponent
import com.musasyihab.simplenews.di.module.ActivityModule
import com.musasyihab.simplenews.model.GetSourcesModel
import com.musasyihab.simplenews.model.SourceModel
import com.musasyihab.simplenews.ui.news.NewsActivity
import com.musasyihab.simplenews.ui.view.ErrorPageView
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private lateinit var sourceListView: RecyclerView
    private lateinit var loading: ProgressBar
    private lateinit var errorPage: ErrorPageView

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: SourceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        initView()
        presenter.getSourceList()
    }

    private fun initView() {
        sourceListView = findViewById(R.id.source_list) as RecyclerView
        loading = findViewById(R.id.source_loading) as ProgressBar
        errorPage = findViewById(R.id.main_error_page) as ErrorPageView

        linearLayoutManager = LinearLayoutManager(this)
        sourceListView.layoutManager = linearLayoutManager

        adapter = SourceListAdapter(Collections.emptyList(), this, SourceListAdapterListener())
        sourceListView.adapter = adapter

        sourceListView.visibility = View.GONE
        errorPage.visibility = View.GONE

        errorPage.setListener(object: ErrorPageView.OnReloadClick {
            override fun clickReload() {
                errorPage.visibility = View.GONE
                presenter.getSourceList()
            }
        })
    }

    private fun loadToView(data: GetSourcesModel) {
        if (!data.sources.isEmpty()) {
            errorPage.visibility = View.GONE
            sourceListView.visibility = View.VISIBLE
            adapter = SourceListAdapter(data.sources, this, SourceListAdapterListener())
            sourceListView.adapter = adapter
        } else {
            errorPage.visibility = View.VISIBLE
            errorPage.setReloadBtnVisibility(false)
            errorPage.setErrorText(getString(R.string.empty_data))
            sourceListView.visibility = View.GONE
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
        errorPage.visibility = View.VISIBLE
        errorPage.setErrorText(getString(R.string.error_message))
        errorPage.setReloadBtnText(getString(R.string.reload))
        errorPage.setReloadBtnVisibility(true)
        sourceListView.visibility = View.GONE
        Toast.makeText(this, "showErrorMessage: "+error, Toast.LENGTH_SHORT).show()
    }

    override fun loadDataSuccess(response: GetSourcesModel) {
        loadToView(response)
    }

    private inner class SourceListAdapterListener : SourceListAdapter.Listener {

        override fun onItemClicked(item: SourceModel, position: Int) {
            val newsIntent = Intent(this@MainActivity, NewsActivity::class.java)
            val sourceId = item.id
            val sourceName = item.name
            newsIntent.putExtra(NewsActivity.EXTRA.SELECTED_SOURCE_ID, sourceId)
            newsIntent.putExtra(NewsActivity.EXTRA.SELECTED_SOURCE_NAME, sourceName)
            startActivity(newsIntent)
        }

    }
}
