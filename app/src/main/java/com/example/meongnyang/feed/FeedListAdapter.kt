package com.example.meongnyang.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meongnyang.R
import com.example.meongnyang.model.FeedModel

class FeedListAdapter(val feedList: ArrayList<FeedModel>): RecyclerView.Adapter<FeedListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedListAdapter.ViewHolder, position: Int) {
        holder.feedName.text = feedList[position].name
        holder.good.text = feedList[position].efficacy.toString()

        // 아이템 클릭 이벤트 -> 자세한 요소
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val feedName: TextView = itemView.findViewById(R.id.feedName)
        val good: TextView = itemView.findViewById(R.id.goodText)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}