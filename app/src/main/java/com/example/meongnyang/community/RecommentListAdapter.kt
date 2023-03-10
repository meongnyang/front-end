package com.example.meongnyang.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meongnyang.R
import com.example.meongnyang.model.CommentModel
import com.example.meongnyang.model.reCommentModel

class RecommentListAdapter(private val recommentList: ArrayList<reCommentModel>): RecyclerView.Adapter<RecommentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recomment_list_layout, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return recommentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.comment.text = recommentList[position].contents
        holder.username.text = recommentList[position].nickname
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val comment: TextView = itemView.findViewById(R.id.list_comment)
        val username: TextView = itemView.findViewById(R.id.username)
    }
}