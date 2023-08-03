package com.nakyung.meongnyang.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.api.RetrofitApi
import com.nakyung.meongnyang.model.*

class CommentListAdpater(private val commentList: ArrayList<AllComment>): RecyclerView.Adapter<CommentListAdpater.ViewHolder>() {
    val retrofit = RetrofitApi.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.comment.text = commentList[position].contents
        holder.username.text = commentList[position].nickname
        holder.rvRecomment.apply {
            adapter = RecommentListAdapter(commentList[position].commentList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        // 아이템 클릭 이벤트 -> 게시글 보여주기
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val comment: TextView = itemView.findViewById(R.id.list_comment)
        val username: TextView = itemView.findViewById(R.id.username)
        val rvRecomment: RecyclerView = itemView.findViewById(R.id.rv_recomment)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}