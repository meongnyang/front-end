package com.example.meongnyang.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meongnyang.R
import com.example.meongnyang.model.Post

class PostListAdapter(val postList: ArrayList<Post>): RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.commu_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) {
        holder.title.text = postList[position].title
        holder.categoryName.text = postList[position].categoryName

        // 아이템 클릭 이벤트 -> 게시글 보여주기
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryTV)
        val title: TextView = itemView.findViewById(R.id.titleTV)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}