package com.yashendra.margapp_2.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yashendra.margapp_2.Activities.RoadmapDetailsActivity
import com.yashendra.margapp_2.Model.RoadmapItem
import com.yashendra.margapp_2.R


class RoadmapAdapter(private val roadmapItemList: List<RoadmapItem>) :
    RecyclerView.Adapter<RoadmapAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewRoadmap = itemView.findViewById<ImageView>(R.id.imageViewRoadmap)
        val textViewRoadmap = itemView.findViewById<TextView>(R.id.textViewRoadmap)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = roadmapItemList[position]
                    val context = itemView.context
                    val intent = Intent(context, RoadmapDetailsActivity::class.java)
                    intent.putExtra("itemName", clickedItem.text)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_roadmap, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = roadmapItemList[position]
        holder.imageViewRoadmap.setImageResource(item.imageResource)
        holder.textViewRoadmap.text = item.text
    }

    override fun getItemCount(): Int {
        return roadmapItemList.size
    }
}
