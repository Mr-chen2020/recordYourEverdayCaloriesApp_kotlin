package com.example.recordeverdaycalories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.recordeverdaycalories.R
import com.google.gson.Gson
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditCalories : AppCompatActivity() {
    private var btn_update: Button? = null
    private var editText_date: TextView? = null
    private var calendarView: CalendarView? = null
    private var editText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_calories)
        btn_update = findViewById<View>(R.id.btn_update) as Button
        editText_date = findViewById<View>(R.id.expressDate) as TextView
        calendarView = findViewById<View>(R.id.calendarView) as CalendarView
        editText = findViewById<View>(R.id.editText) as EditText


        btn_update!!.setOnClickListener(View.OnClickListener {
            //1.将日期和卡路里数据保存
            val simpleFormatter = SimpleDateFormat("yyyy-MM-dd")
            val date = editText_date!!.text.toString()
            var Calories = editText!!.text.toString()

            if (date == null || date == "") {
                Toast.makeText(this@EditCalories, "日期尚为空，请输入", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (Calories == null || Calories == "") {
                Calories = "0"
                //                    Log.d(TAG, "onClick: ");
                Toast.makeText(this@EditCalories, "卡路里尚为空,请输入", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            var d: Date? = null
            try {
                d = simpleFormatter.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val c = CaloriesTimescale(d!!, Calories.toInt())
//            val dataParse = dataParse()
            dataParse.addItem(c)
            val gson = Gson()
            dataParse.writeDataToFile(this@EditCalories, gson.toJson(dataParse.getNowCT()))

            Log.d("对象转json: ", gson.toJson(dataParse.getNowCT()))

            //2.然后back到上一个活动
            //                finish();
            val intent = Intent(this@EditCalories, CalendarAndCalories::class.java)
            startActivity(intent)
            finish()
        })
        calendarView!!.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar[year, month] = dayOfMonth
            val simpleFormatter = SimpleDateFormat("yyyy-MM-dd")
            val date = simpleFormatter.format(calendar.time)
            //
            editText_date!!.text = date
            //显示用户选择的日期
            Toast.makeText(
                this@EditCalories,
                year.toString() + "年" + (month + 1) + "月" + dayOfMonth + "日",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //将更新的json写回record.json中
    fun writeDataToFile(string: String?) {
        var string = string
        var out: FileOutputStream? = null
        var Writer: BufferedWriter? = null
        if (string == null) {
            string = ""
        }
        try {
            Log.d("mywriter", "writeDataToFile: $string")
            out = openFileOutput("record.json", MODE_PRIVATE)
            Writer = BufferedWriter(OutputStreamWriter(out))
            Writer.write(string)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                Writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}