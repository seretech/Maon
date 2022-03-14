package com.developers.serenity.maontech.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.developers.serenity.maontech.ModelClass
import com.developers.serenity.maontech.R
import com.squareup.picasso.Picasso

class ModelAdapter(private val context: Context,
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
            convertView = inflater.inflate(R.layout.model_adapter, null, true)

            holder.img = convertView!!.findViewById<View>(R.id.img) as ImageView
            holder.title = convertView.findViewById<View>(R.id.t) as TextView
            holder.country = convertView.findViewById<View>(R.id.c) as TextView
            holder.date = convertView.findViewById<View>(R.id.d) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val ty = arrayList[position].getTypes()

        if(ty.contains("Com", ignoreCase = false)){
            holder.img!!.visibility = View.GONE

            holder.title!!.text = arrayList[position].getTitles()
            holder.country!!.text = arrayList[position].getCountries()
            holder.date!!.text = "Start Date: ${arrayList[position].getDates()}"


        } else if(ty.contains("Teams", ignoreCase = true)){
            holder.title!!.visibility = View.GONE
            holder.country!!.visibility = View.GONE
            holder.date!!.visibility = View.GONE

            val im = arrayList[position].getImgs()

            if(im == ""){
                Picasso.get()
                    .load("na")
                    .fit()
                    .placeholder(R.drawable.bg_gr)
                    .centerInside()
                    .into(holder.img)
            } else {
                Picasso.get()
                    .load(arrayList[position].getImgs())
                    .fit()
                    .placeholder(R.drawable.bg_gr)
                    .centerInside()
                    .into(holder.img)
            }


        }


        return convertView
    }

    private inner class ViewHolder {

        var title: TextView? = null
        var country: TextView? = null
        var date: TextView? = null
        var img: ImageView? = null
    }

}


