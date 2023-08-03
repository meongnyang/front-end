package com.nakyung.meongnyang.map
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nakyung.meongnyang.R
import com.nakyung.meongnyang.model.MapList

class MapListAdapter(val mapList: ArrayList<MapList>): RecyclerView.Adapter<MapListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_maplist_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mapList.size
    }

    override fun onBindViewHolder(holder: MapListAdapter.ViewHolder, position: Int) {
        holder.name.text = mapList[position].name
        holder.road.text = mapList[position].road
        holder.phone.text = mapList[position].phone

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.hos_name)
        val road: TextView = itemView.findViewById(R.id.hos_road)
        val phone: TextView = itemView.findViewById(R.id.hos_phone)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener
}