package com.musasyihab.simplenews.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.musasyihab.simplenews.R
import com.musasyihab.simplenews.model.SourceModel

class SourceListAdapter(private val mList: List<SourceModel>, context: Context, adapterListener: SourceListAdapter.Listener) : RecyclerView.Adapter<SourceListAdapter.SourceHolder>() {

    private val mInflater: LayoutInflater
    private val adapterListener: SourceListAdapter.Listener
    private val listener: SourceHolderListener

    init {
        mInflater = LayoutInflater.from(context)
        this.adapterListener = adapterListener
        listener = SourceHolderListener()
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: SourceListAdapter.SourceHolder, position: Int) {
        val item = mList[position]
        holder.bindItem(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceListAdapter.SourceHolder {
        val inflatedView = mInflater.inflate(R.layout.source_item_layout, parent,false)
        return SourceHolder(inflatedView, listener)
    }

    interface Listener {
        fun onItemClicked(item: SourceModel, position: Int)
    }

    class SourceHolder(v: View, private var listener: Listener) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var item: SourceModel? = null

        var sourceName: TextView? = null
        var description: TextView? = null
        var layout: LinearLayout? = null

        init {
            sourceName = view.findViewById(R.id.source_item_name) as TextView
            description = view.findViewById(R.id.source_item_desc) as TextView
            layout = view.findViewById(R.id.source_item_layout) as LinearLayout
        }

        fun bindItem(item: SourceModel) {
            this.item = item
            sourceName!!.text = item.name
            description!!.text = item.description

            layout!!.setOnClickListener { listener.onItemClicked(item, adapterPosition) }
        }

        interface Listener {
            fun onItemClicked(item: SourceModel, position: Int)
        }

    }

    private inner class SourceHolderListener : SourceHolder.Listener {

        override fun onItemClicked(item: SourceModel, position: Int) {
            adapterListener.onItemClicked(item, position)
        }
    }
}