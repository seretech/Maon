package com.developers.serenity.maontech.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.developers.serenity.maontech.ModelClass
import com.developers.serenity.maontech.R
import com.developers.serenity.maontech.adapters.ModelAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tb.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.coroutines.suspendCoroutine

class Players : AppCompatActivity() {

    private lateinit var arrayList: ArrayList<ModelClass>
    private var adapter: ModelAdapter? = null

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ti.text = getString(R.string.teams)
        bk.setOnClickListener {
            onBackPressed()
        }

        val bundle: Bundle? = intent.extras
        id = bundle?.getInt("id", 0)!!

        loadData()

        gd.setOnItemClickListener { _: AdapterView<*>?,
                                    _: View?, i: Int, _: Long ->
            val mc: ModelClass = arrayList[i]
            val id: Int = mc.getId()
            val title: String = mc.getTitles()
            val intent = Intent(this@Players, PlayerInfo::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", title)
            startActivity(intent)
        }
    }

    private fun loadData() {
        val job = Job()

        val errorHandler = CoroutineExceptionHandler { _, _ ->
            loadData0()
        }
        val scope = CoroutineScope(job + Dispatchers.Main)

        scope.launch(errorHandler) {
            getData()
        }

    }

    private suspend fun getData() = suspendCoroutine<String> {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.football-data.org/v2/competitions/$id/teams"
        val req = object: StringRequest(
            Method.GET, url,
            { response ->
                val data = response.toString()
                val jsonObj = JSONObject(data)
                arrayList = ArrayList()

                if (jsonObj.optJSONArray("teams") == null) {
                    Toast.makeText(this, "No Data Retrieved", Toast.LENGTH_SHORT).show()
                    prog.visibility = View.GONE
                } else {
                    val dataArray = jsonObj.getJSONArray("teams")
                      for (i in 0 until dataArray.length()) {
                        val mc = ModelClass()
                        val obj = dataArray.getJSONObject(i)
                          mc.setId(obj.getInt("id"))
                          mc.setImgs(obj.optString("crestUrl", "na"))
                          mc.setTitles(obj.getString("shortName"))
                        mc.setTypes("Teams")

                        arrayList.add(mc)
                    }
                    adapter = ModelAdapter(this, arrayList)
                    gd!!.adapter = adapter
                    prog.visibility = View.GONE
                }


            }, {err->
                (
                        if(err.networkResponse == null){
                            showErr1()
                        } else {
                            loadData0()
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

    private fun loadData0() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.football-data.org/v2/competitions/$id/teams"
        val req = object: StringRequest(
            Method.GET, url,
            { response ->
                val data = response.toString()
                val jsonObj = JSONObject(data)
                arrayList = ArrayList()

                if (jsonObj.optJSONArray("teams") == null) {
                    Toast.makeText(this, "No Data Retrieved", Toast.LENGTH_SHORT).show()
                    prog.visibility = View.GONE
                } else {
                    val dataArray = jsonObj.getJSONArray("teams")
                      for (i in 0 until dataArray.length()) {
                        val mc = ModelClass()
                        val obj = dataArray.getJSONObject(i)
                          mc.setId(obj.getInt("id"))
                          mc.setImgs(obj.optString("crestUrl", "na"))
                          mc.setTitles(obj.getString("shortName"))
                        mc.setTypes("Teams")

                        arrayList.add(mc)
                    }
                    adapter = ModelAdapter(this, arrayList)
                    gd!!.adapter = adapter
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