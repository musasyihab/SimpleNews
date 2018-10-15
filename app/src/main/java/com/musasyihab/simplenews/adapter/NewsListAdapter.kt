package com.musasyihab.simplenews.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.model.ArticleModel

class NewsListAdapter(private val mList: List<ArticleModel>, context: Context, adapterListener: NewsListAdapter.Listener) : RecyclerView.Adapter<NewsListAdapter.NewsHolder>() {

    private val mInflater: LayoutInflater
    private val adapterListener: NewsListAdapter.Listener
    private val listener: NewsHolderListener

    init {
        mInflater = LayoutInflater.from(context)
        this.adapterListener = adapterListener
        listener = NewsHolderListener()
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: NewsListAdapter.NewsHolder, position: Int) {
        val item = mList[position]
        holder.bindItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapter.NewsHolder {
        val inflatedView = mInflater.inflate(R.layout.news_item_layout, parent,false)
        return NewsHolder(inflatedView, listener)
    }

    interface Listener {
        fun onItemClicked(item: ArticleModel, position: Int)
    }

    class NewsHolder(v: View, private var listener: Listener) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var item: ArticleModel? = null

        var title: TextView? = null
        var content: TextView? = null
        var image: ImageView? = null
        var layout: LinearLayout? = null
        var glideContext: RequestManager? = null

        init {
            title = view.findViewById(R.id.news_item_title) as TextView
            content = view.findViewById(R.id.news_item_content) as TextView
            image = view.findViewById(R.id.news_item_image) as ImageView
            layout = view.findViewById(R.id.news_item_layout) as LinearLayout

            glideContext = Glide.with(view.context)
        }

        fun bindItem(item: ArticleModel) {
            this.item = item
            title!!.text = item.title
            content!!.text = item.content

            glideContext!!.load(item.urlToImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(R.drawable.ic_image)
                    .into(image);

            layout!!.setOnClickListener { listener.onItemClicked(item, adapterPosition) }
        }

        interface Listener {
            fun onItemClicked(item: ArticleModel, position: Int)
        }

    }

    private inner class NewsHolderListener : NewsHolder.Listener {

        override fun onItemClicked(item: ArticleModel, position: Int) {
            adapterListener.onItemClicked(item, position)
        }
    }
}