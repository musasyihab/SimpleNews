package com.musasyihab.simplenews.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.di.component.DaggerActivityComponent
import com.musasyihab.simplenews.di.module.ActivityModule
import com.musasyihab.simplenews.model.GetSourcesModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        presenter.getSourceList()
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .build()

        activityComponent.inject(this)
    }

    override fun showProgress(show: Boolean) {
        Toast.makeText(this, "showProgress", Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage(error: String) {
        Toast.makeText(this, "showErrorMessage: "+error, Toast.LENGTH_SHORT).show()
    }

    override fun loadDataSuccess(response: GetSourcesModel) {
        Toast.makeText(this, "loadDataSuccess", Toast.LENGTH_SHORT).show()
    }
}
