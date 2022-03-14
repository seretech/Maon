package com.developers.serenity.maontech.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.developers.serenity.maontech.ModelClass
import com.developers.serenity.maontech.R
import com.developers.serenity.maontech.adapters.ModelAdapter2
import com.developers.serenity.maontech.adapters.ModelAdapter3
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player_info.*
import kotlinx.android.synthetic.main.activity_player_info.prog
import kotlinx.android.synthetic.main.tb.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class PlayerInfo : AppCompatActivity() {

    private lateinit var arrayList: ArrayList<ModelClass>
    private var adapter2: ModelAdapter2? = null
    private var adapter3: ModelAdapter3? = null

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_info)

        val bundle: Bundle? = intent.extras
        id = bundle?.getInt("id", 0)!!

        ti.text = bundle.getString("title","")!!
        bk.setOnClickListener {
            onBackPressed()
        }

        prog.bringToFront()

        loadData()
    }

    private fun loadData() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.football-data.org/v2/teams/$id"
        val req = @SuppressLint("SetTextI18n")
        object: StringRequest(
            Method.GET, url,
            { response ->
                val data = response.toString()
                val jsonObj = JSONObject(data)
                d1.text = "01-01-${jsonObj.optString("founded", "N/A")}"
                d2.text = jsonObj.optString("shortName", "N/A")
                d3.text = jsonObj.optString("address", "N/A")
                d4.text = jsonObj.optString("phone", "N/A")
                d5.text = jsonObj.optString("website", "N/A")
                d6.text = jsonObj.optString("email", "N/A")
                val col = jsonObj.optString("clubColors", "N/A").replace("/",",").replace(" ","")

                Picasso.get()
                    .load(jsonObj.optString("crestUrl", "N/A"))
                    .fit()
                    .placeholder(R.color.app_green)
                    .centerInside()
                    .into(img)

                val stringArrayList = ArrayList(mutableListOf(*col.split(",").toTypedArray()))

                adapter3 = ModelAdapter3(this, stringArrayList)
                tw!!.adapter = adapter3

                arrayList = ArrayList()

                if (jsonObj.optJSONArray("squad") == null) {
                    Toast.makeText(this, "No Data Retrieved", Toast.LENGTH_SHORT).show()
                    prog.visibility = View.GONE
                } else {
                    val dataArray = jsonObj.getJSONArray("squad")
                    for (i in 0 until dataArray.length()) {
                        val mc = ModelClass()
                        val obj = dataArray.getJSONObject(i)
                        mc.setNames(obj.optString("name", "N/A"))
                        mc.setTitles(obj.optString("position", "N/A"))
                        mc.setCountries(obj.optString("countryOfBirth", "N/A"))
                        mc.setDates(obj.optString("dateOfBirth", "N/A").replace("T00:00:00Z",""))

                        arrayList.add(mc)
                    }
                    adapter2 = ModelAdapter2(this, arrayList)
                    ls!!.adapter = adapter2
                    prog.visibility = View.GONE
                }


            }, {
                (
                        loadData0()

                        )
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Auth-Token"] = "2cb71662b6dc4fa48466ecc8008a55a9"
                return headers
            }
        }

        queue.add(req)
    }

    private fun loadData0() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.football-data.org/v2/teams/$id"
        val req = @SuppressLint("SetTextI18n")
        object: StringRequest(
            Method.GET, url,
            { response ->
                val data = response.toString()
                val jsonObj = JSONObject(data)
                d1.text = "01-01-${jsonObj.optString("founded", "N/A")}"
                d2.text = jsonObj.optString("shortName", "N/A")
                d3.text = jsonObj.optString("address", "N/A")
                d4.text = jsonObj.optString("phone", "N/A")
                d5.text = jsonObj.optString("website", "N/A")
                d6.text = jsonObj.optString("email", "N/A")
                val col = jsonObj.optString("clubColors", "N/A").replace("/",",").replace(" ","")

                Picasso.get()
                    .load(jsonObj.optString("crestUrl", "N/A"))
                    .fit()
                    .placeholder(R.color.app_green)
                    .centerInside()
                    .into(img)

                val stringArrayList = ArrayList(mutableListOf(*col.split(",").toTypedArray()))

                adapter3 = ModelAdapter3(this, stringArrayList)
                tw!!.adapter = adapter3

                arrayList = ArrayList()

                if (jsonObj.optJSONArray("squad") == null) {
                    Toast.makeText(this, "No Data Retrieved", Toast.LENGTH_SHORT).show()
                    prog.visibility = View.GONE
                } else {
                    val dataArray = jsonObj.getJSONArray("squad")
                    for (i in 0 until dataArray.length()) {
                        val mc = ModelClass()
                        val obj = dataArray.getJSONObject(i)
                        mc.setNames(obj.optString("name", "N/A"))
                        mc.setTitles(obj.optString("position", "N/A"))
                        mc.setCountries(obj.optString("countryOfBirth", "N/A"))
                        mc.setDates(obj.optString("dateOfBirth", "N/A").replace("T00:00:00Z",""))

                        arrayList.add(mc)
                    }
                    adapter2 = ModelAdapter2(this, arrayList)
                    ls!!.adapter = adapter2
                    prog.visibility = View.GONE
                }


            }, { err ->
                (
                        if(err.networkResponse == null){
                            showErr1()
                        } else {
                            showErr(err.networkResponse.data)
                        }

                        )
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-Auth-Token"] = "2cb71662b6dc4fa48466ecc8008a55a9"
                return headers
            }
        }

        queue.add(req)
    }

    private fun showErr(msg: ByteArray) {
        prog.visibility = View.GONE
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        @SuppressLint("InflateParams") val view: View =
            layoutInflater.inflate(R.layout.bottom_sheet_single, null)
        bottomSheetDialog.setContentView(view)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics)
                .toInt()
        bottomSheetDialog.show()

        val txt1 = view.findViewById<TextView>(R.id.txt)
        val js = String(msg)

        val er: String = try {
            val jsonObject = JSONObject(js)
            jsonObject.getString("errorCode")
        } catch (e: JSONException) {
            ""+e.localizedMessage
        }

        if(er == "403"  || er == "429"){
            txt1.text = getString(R.string.err2)
            txt1.setOnClickListener {
                bottomSheetDialog.dismiss()
                onBackPressed()
            }
        } else {
            txt1.text = getString(R.string.click_to_retry)
            txt1.setOnClickListener {
                prog.visibility = View.VISIBLE
                bottomSheetDialog.dismiss()
                loadData()
            }
        }

    }

    private fun showErr1() {
        prog.visibility = View.GONE
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        @SuppressLint("InflateParams") val view: View =
            layoutInflater.inflate(R.layout.bottom_sheet_single, null)
        bottomSheetDialog.setContentView(view)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics)
                .toInt()
        bottomSheetDialog.show()

        val txt1 = view.findViewById<TextView>(R.id.txt)

        txt1.text = getString(R.string.click_to_retry)

        txt1.setOnClickListener {
            prog.visibility = View.VISIBLE
            bottomSheetDialog.dismiss()
            loadData()
        }

    }



}