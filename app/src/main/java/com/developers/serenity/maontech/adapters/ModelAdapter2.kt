package com.developers.serenity.maontech.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.developers.serenity.maontech.ModelClass
import com.developers.serenity.maontech.R

class ModelAdapter2(private val context: Context,
                    private val arrayList: ArrayList<ModelClass>) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.model_adapter2, null, true)

            holder.name = convertView!!.findViewById<View>(R.id.n) as TextView
            holder.title = convertView.findViewById<View>(R.id.t) as TextView
            holder.country = convertView.findViewById<View>(R.id.c) as TextView
            holder.date = convertView.findViewById<View>(R.id.d) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.name!!.text = arrayList[position].getNames()
        holder.title!!.text = arrayList[position].getTitles()
        holder.country!!.text = arrayList[position].getCountries()
        holder.date!!.text = arrayList[position].getDates()

        return convertView
    }

    private inner class ViewHolder {

        var title: TextView? = null
        var name: TextView? = null
        var country: TextView? = null
        var date: TextView? = null
    }

}


