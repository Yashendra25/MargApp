package com.yashendra.margapp_2.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.yashendra.margapp_2.R

class CustomArrayAdapter(
    context: Context,
    resource: Int,
    objects: List<String>
) : ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_spinner_item, null)
        val textView = view.findViewById<TextView>(R.id.spinner_item_text)
        textView.text = getItem(position)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
