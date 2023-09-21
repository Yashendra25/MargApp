package com.yashendra.margapp_2.Adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yashendra.margapp_2.Model.SubjectItem
import com.yashendra.margapp_2.R


class SubjectAdapter(private val subjectItemList: List<SubjectItem>) :
    RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewSubject = itemView.findViewById(R.id.imageViewSubject) as ImageView
        val textViewSubject = itemView.findViewById(R.id.textViewSubject) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_subject, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = subjectItemList[position]
        holder.imageViewSubject.setImageResource(item.imageResource)
        holder.textViewSubject.text = item.text
    }

    override fun getItemCount(): Int {
        return subjectItemList.size
    }
}
