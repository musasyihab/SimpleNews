package com.musasyihab.simplenews.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.musasyihab.simplenews.R

class ErrorPageView(private val mContext: Context, mAttributes: AttributeSet): LinearLayout(mContext, mAttributes) {

    private var reloadButton: Button
    private var errorText: TextView
    private var onReloadClick: OnReloadClick? =null

    init {

        LayoutInflater.from(mContext).inflate(R.layout.error_page_layout, this, true)

        reloadButton = findViewById<Button>(R.id.reload_btn) as Button
        errorText = findViewById<TextView>(R.id.error_text) as TextView

        val attributes = mContext.obtainStyledAttributes(mAttributes,
                R.styleable.ErrorPageView)
        val errorText = attributes.getString(R.styleable.ErrorPageView_errorText)
        val reloadBtnText = attributes.getString(R.styleable.ErrorPageView_reloadBtnText)
        val isShowReload = attributes.getBoolean(R.styleable.ErrorPageView_showReload, true)

        val defaultErrorText = mContext.getString(R.string.empty_data)
        val defaultReloadBtnText = mContext.getString(R.string.reload)

        this.reloadButton.text = if(reloadBtnText?.isEmpty() == true) reloadBtnText else defaultReloadBtnText
        this.errorText.text = if(errorText?.isEmpty() == true) errorText else defaultErrorText
        this.reloadButton.visibility = if(isShowReload) View.VISIBLE else View.GONE

        reloadButton.setOnClickListener {
            onReloadClick!!.clickReload()
        }

    }

    fun setErrorText(text: String) {
        errorText.text = text
    }
    fun setReloadBtnText(text: String) {
        reloadButton.text = text
    }
    fun setReloadBtnVisibility(isVisible: Boolean) {
        reloadButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
    fun setListener(listener: OnReloadClick) {
        onReloadClick = listener
    }

    interface OnReloadClick {
        fun clickReload()
    }

}
