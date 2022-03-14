package com.developers.serenity.maontech.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.developers.serenity.maontech.R
import com.developers.serenity.maontech.R.drawable

class ModelAdapter3(private val context: Context,
                    private val arrayList: ArrayList<String>) : BaseAdapter() {

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
            convertView = inflater.inflate(R.layout.colors_adapter, null, true)

            holder.img = convertView!!.findViewById<View>(R.id.i) as ImageView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        when (arrayList[position]) {
            "Blue" -> {
                holder.img?.setBackgroundResource(drawable.bg_blu)
            }
            "Black" -> {
                holder.img?.setBackgroundResource(drawable.bg_bla)
            }
            "White" -> {
                holder.img?.setBackgroundResource(drawable.bg_wh)
            }
            "Yellow" -> {
                holder.img?.setBackgroundResource(drawable.bg_yel)
            }
            "RoyalBlue" -> {
                holder.img?.setBackgroundResource(drawable.bg_ry)
            }
            "Gold" -> {
                holder.img?.setBackgroundResource(drawable.bg_gd)
            }
            "SkyBlue" -> {
                holder.img?.setBackgroundResource(drawable.bg_sky)
            }
            "Green" -> {
                holder.img?.setBackgroundResource(drawable.bg_gr)
            }
            "Claret" -> {
                holder.img?.setBackgroundResource(drawable.bg_cla)
            }
            "NavyBlue" -> {
                holder.img?.setBackgroundResource(drawable.bg_navy)
            }
            "Red" -> {
                holder.img?.setBackgroundResource(drawable.bg_red)
            }
            else -> {
                holder.img?.setBackgroundResource(drawable.bg_wh)
            }
        }


        return convertView
    }

    private inner class ViewHolder {

        var img: ImageView? = null
    }

}



