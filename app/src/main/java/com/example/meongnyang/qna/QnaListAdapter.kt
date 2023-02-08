package com.example.meongnyang.qna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meongnyang.R
import com.example.meongnyang.model.Qna

class QnaListAdapter(val qnaList: ArrayList<String>): RecyclerView.Adapter<QnaListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnaListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.qna_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return qnaList.size
    }

    override fun onBindViewHolder(holder: QnaListAdapter.ViewHolder, position: Int) {
        holder.question.text = qnaList[position]

        // 아이템 클릭 이벤트 -> 질의응답 보여주기
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.questionText)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}