package com.example.recordeverdaycalories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recordeverdaycalories.R
import com.google.gson.Gson
import java.util.*

class CalendarAndCalories : AppCompatActivity() {
    private var btn_add: Button? = null //“添加数据”按钮

    //测试
    private val ct: List<CaloriesTimescale> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var testD: CaloriesTimescaleAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_and_calories)

        //读取文件获得卡路里数据

//        ct = dataParse.readDataFromFile(CalendarAndCalories.this);
        dataParse.readDataFromFile(this@CalendarAndCalories)
        Log.d("mywriter", "onCreate: " + dataParse.getNowCT())
        btn_add = findViewById<View>(R.id.btn_add) as Button
        recyclerView = findViewById<View>(R.id.recycle_view) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this@CalendarAndCalories)
        recyclerView!!.layoutManager = linearLayoutManager

        testD = dataParse.getNowCT()?.let { CaloriesTimescaleAdapter(it) }
        recyclerView!!.adapter = testD
        btn_add!!.setOnClickListener {
            val intent = Intent(this@CalendarAndCalories, EditCalories::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position: Int
        position = testD?.contextMenuPosition as Int

        when (item.itemId) {
            0 -> {
                val calories: Int = dataParse.getNowCT()!![position].calories
                if (calories > 3000) {
                    Toast.makeText(
                        this@CalendarAndCalories,
                        "你摄入了" + calories + "卡路里,数值偏高，请注意节制，保持合理膳食哦！",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (calories > 1500) {
                    Toast.makeText(
                        this@CalendarAndCalories,
                        "你摄入了" + calories + "卡路里,数值居中，请继续保持哦，每天好身材！",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@CalendarAndCalories,
                        "你摄入了" + calories + "卡路里,数值偏低，请务必吃多一点，减肥不是节食，而是运动健身哦！",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            1 -> {
                dataParse.delItem(position)
                Toast.makeText(
                    this@CalendarAndCalories,
                    item.title.toString() + "" + position,
                    Toast.LENGTH_SHORT
                ).show()
            }
            2 -> Toast.makeText(this@CalendarAndCalories, "暂时还不支持，抱歉", Toast.LENGTH_SHORT).show()
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        val gson = Gson()
        //        String s = gson.toJson(ct);
        val s = gson.toJson(dataParse.getNowCT())
        dataParse.writeDataToFile(this@CalendarAndCalories, s)
    }
}