package com.example.meongnyang.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meongnyang.R
import com.example.meongnyang.api.RetrofitApi
import com.example.meongnyang.model.Comment
import com.example.meongnyang.model.CommentModel
import com.example.meongnyang.model.Post
import org.w3c.dom.Text

class CommentListAdpater(private val commentList: ArrayList<CommentModel>): RecyclerView.Adapter<CommentListAdpater.ViewHolder>() {
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
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val comment: TextView = itemView.findViewById(R.id.list_comment)
        val username: TextView = itemView.findViewById(R.id.username)
    }
}