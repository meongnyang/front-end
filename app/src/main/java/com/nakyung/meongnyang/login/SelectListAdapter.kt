package com.nakyung.meongnyang.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.model.PetList

class SelectListAdapter(private val petList: ArrayList<PetList>): RecyclerView.Adapter<SelectListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.login_list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: SelectListAdapter.ViewHolder, position: Int) {
        holder.petName.text = petList[position].name
        holder.petBirth.text = petList[position].birth
        holder.petSpec.text = petList[position].species

        // 아이템 클릭 이벤트 -> 게시글 보여주기
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val petName: TextView = itemView.findViewById(R.id.petName)
        val petBirth: TextView = itemView.findViewById(R.id.petBirth)
        val petSpec: TextView = itemView.findViewById(R.id.petSpec)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}