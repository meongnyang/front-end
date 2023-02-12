package com.example.meongnyang.feed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meongnyang.R
import com.example.meongnyang.model.FeedModel

class FeedListAdapter(private val feedList: ArrayList<FeedModel>): RecyclerView.Adapter<FeedListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedListAdapter.ViewHolder, position: Int) {
        val data = feedList[position]
        holder.apply {
            Glide.with(itemView.context)
                .load(data.img)
                .override(150, 150)
                .into(holder.feedImg)
            feedName.text = data.name
            good.text = data.efficacy
        }

        // 아이템 클릭 이벤트 -> 자세한 요소
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        // Preload
        if (position <= feedList.size) {
            val endPosition = if (position + 6 > feedList.size) {
                feedList.size
            } else {
                position + 6
            }
            feedList.subList(position, endPosition).map { it.img }.forEach {
                preload(holder.itemView.context, it)
            }
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val feedName: TextView = itemView.findViewById(R.id.name)
        val good: TextView = itemView.findViewById(R.id.goodText)
        val feedImg: ImageView = itemView.findViewById(R.id.feedImg)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun preload(context: Context,  url : String) {
        Glide.with(context).load(url)
            .preload(150, 150)
    }

    private lateinit var itemClickListener : OnItemClickListener
}