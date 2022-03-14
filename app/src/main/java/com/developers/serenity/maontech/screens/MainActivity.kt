package com.developers.serenity.maontech.screens

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
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

class MainActivity : AppCompatActivity() {

    private var dialog: AlertDialog? = null

    private lateinit var arrayList: ArrayList<ModelClass>
    private var adapter: ModelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ti.text = getString(R.string.coms)
        bk.visibility = View.GONE

        if (isValid() == "valid") {
            loadData()
        } else {
            loadSaved()
        }

        gd.setOnItemClickListener { _: AdapterView<*>?,
                                    _: View?, i: Int, _: Long ->
            val mc: ModelClass = arrayList[i]
            val id: Int = mc.getId()
            val intent = Intent(this@MainActivity, Players::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun loadSaved() {
        val sh = getSharedPreferences("Sh", MODE_PRIVATE)

        val gs = Gson()
        val js: String? = sh.getString("comp", null)

        val ty = object : TypeToken<java.util.ArrayList<ModelClass?>?>() {}.type
        arrayList = gs.fromJson(js, ty)

        if (arrayList.size == 0) {
            arrayList = java.util.ArrayList<ModelClass>()
            loadData()
        } else {
            adapter = ModelAdapter(this, arrayList)
            gd!!.adapter = adapter
            prog.visibility = View.GONE
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
        val url = "http://api.football-data.org/v2/competitions/"
        val req = StringRequest(Request.Method.GET, url,
            { response ->
                val data = response.toString()
                val jsonObj = JSONObject(data)
                arrayList = ArrayList()

                if (jsonObj.optJSONArray("competitions") == null) {
                    Toast.makeText(this, "No Data Retrieved", Toast.LENGTH_SHORT).show()
                    prog.visibility = View.GONE
                } else {
                    val dataArray = jsonObj.getJSONArray("competitions")
                    for (i in 0 until dataArray.length()) {
                        val mc = ModelClass()
                        val obj = dataArray.getJSONObject(i)
                        mc.setId(obj.getInt("id"))
                        mc.setTitles(obj.getString("name"))
                        mc.setTypes("Com")
                        val da = obj.getString("currentSeason")
                        if (da.equals("null")) {
                            mc.setDates("Pending")
                        } else {
                            mc.setDates(obj.getJSONObject("currentSeason").getString("startDate"))
                        }

                        mc.setCountries(obj.getJSONObject("area").getString("name"))

                        arrayList.add(mc)
                    }
                    adapter = ModelAdapter(this, arrayList)
                    gd!!.adapter = adapter
                    prog.visibility = View.GONE

                    val sharedPreferences = getSharedPreferences("Sh", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val json = gson.toJson(arrayList)
                    editor.putString("comp", json)
                    editor.apply()
                }

            }, {
                (
                        loadData0()

                        )
            }

        )

        queue.add(req)
    }

    private fun loadData0() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.football-data.org/v2/competitions/"
        val req = StringRequest(Request.Method.GET, url,
            { response ->
                val data = response.toString()
                val jsonObj = JSONObject(data)
                arrayList = ArrayList()

                if (jsonObj.optJSONArray("competitions") == null) {
                    Toast.makeText(this, "No Data Retrieved", Toast.LENGTH_SHORT).show()
                    prog.visibility = View.GONE
                } else {
                    val dataArray = jsonObj.getJSONArray("competitions")
                    // for (i in 0 until dataArray.length()) {
                    for (i in 0 until 60) {
                        val mc = ModelClass()
                        val obj = dataArray.getJSONObject(i)
                        mc.setId(obj.getInt("id"))
                        mc.setTitles(obj.getString("name"))
                        mc.setTypes("Com")
                        val da = obj.getString("currentSeason")
                        if (da.equals("null")) {
                            mc.setDates("Pending")
                        } else {
                            mc.setDates(obj.getJSONObject("currentSeason").getString("startDate"))
                        }

                        mc.setCountries(obj.getJSONObject("area").getString("name"))

                        arrayList.add(mc)
                    }
                    adapter = ModelAdapter(this, arrayList)
                    gd!!.adapter = adapter
                    prog.visibility = View.GONE

                    val sharedPreferences = getSharedPreferences("Sh", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val json = gson.toJson(arrayList)
                    editor.putString("comp", json)
                    editor.apply()
                }


            }, { err ->
                (
                        if (err.networkResponse == null) {
                            showErr1()
                        } else {
                            showErr(err.networkResponse.data)
                        }

                        )
            }

        )

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
            "" + e.localizedMessage
        }

        if (er == "403") {
            txt1.text = getString(R.string.err2)
            txt1.setOnClickListener {
                bottomSheetDialog.dismiss()
                onBackPressed()
            }
        } else {
            Toast.makeText(this, "" + er, Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder
            .setMessage(getString(R.string.sure_to_exit))
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.no)
            ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setNegativeButton(
                getString(R.string.yes_exit)
            ) { _: DialogInterface?, _: Int -> finishAffinity() }
        dialog = alertDialogBuilder.create()
        dialog!!.show()
    }

    private fun isValid(): String {
        var def = ""
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences("Chk", MODE_PRIVATE)
        val data = sharedPreferences.getLong("chk", 0)
        val data1 = System.currentTimeMillis()
        when {
            data == 0L -> {
                def = "valid"
                val editor = sharedPreferences.edit()
                editor.putLong("chk", System.currentTimeMillis() + 60000)
                editor.apply()
            }
            data < data1 -> {
                def = "valid"
                val editor = sharedPreferences.edit()
                editor.putLong("chk", System.currentTimeMillis() + 60000)
                editor.apply()
            }
            data > data1 -> {
                def = "invalid"
            }
        }
        return def
    }
}