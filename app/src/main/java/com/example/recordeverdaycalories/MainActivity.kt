package com.example.recordeverdaycalories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var btn_edit: Button? = null //“编辑”按钮
    private var btn_begin: Button? = null //“输入开始日期”按钮
    private var btn_end: Button? = null //“输入结束日期”按钮
    private var calendarView2: CalendarView? = null
    private var date_start: TextView? = null
    private var date_end: TextView? = null
    private var btn_query: Button? = null //“查询”按钮
    private var resultSum: TextView? = null //“查询结果”
    private var graphView: GraphView? = null //图表

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_edit = findViewById<View>(R.id.btn_edit) as Button
        btn_begin = findViewById<View>(R.id.btn_begin) as Button
        btn_end = findViewById<View>(R.id.btn_end) as Button
        date_start = findViewById(R.id.date_start) as TextView
        date_end = findViewById(R.id.date_end) as TextView
        btn_query = findViewById<View>(R.id.btn_check) as Button
        resultSum = findViewById<View>(R.id.resultSum) as TextView
        graphView = findViewById<View>(R.id.graph) as GraphView

        //先将文件存储的内容读出来
        dataParse.readDataFromFile(this@MainActivity)

        btn_begin!!.setOnClickListener { v -> //初始化布局activity_popupWindow.xml
            val layoutInflater = LayoutInflater.from(this@MainActivity)
            val contentView: View = layoutInflater.inflate(R.layout.calendar, null)
            //对布局里面的控件进行初始化并进行相应的操作
            //初始化PopupWindow
            val popupWindow = PopupWindow(
                contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.isTouchable = true
            popupWindow.setTouchInterceptor { v, event -> false }
            popupWindow.showAsDropDown(v)
            calendarView2 = contentView.findViewById(R.id.calendarView2) as CalendarView
            calendarView2!!.setOnDateChangeListener(OnDateChangeListener { calendarView2, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar[year, month] = dayOfMonth
                val simpleFormatter = SimpleDateFormat("yyyy-MM-dd")
                val date = simpleFormatter.format(calendar.time)
                date_start!!.setText(date)
                resultSum!!.text = ""
                //显示用户选择的日期
                Toast.makeText(
                    this@MainActivity,
                    year.toString() + "年" + (month + 1) + "月" + dayOfMonth + "日",
                    Toast.LENGTH_SHORT
                ).show()
            })
            val text = btn_begin!!.text.toString()
            if (text == "click") {
                btn_begin!!.text = "取消选择"
            } else {
                btn_begin!!.text = "click"
            }
        }
        btn_end!!.setOnClickListener { v ->
            val text = btn_end!!.text.toString()
            if (text == "click") {
                btn_end!!.text = "取消选择"
            } else {
                btn_end!!.text = "click"
            }

            //初始化布局activity_popupWindow.xml
            val layoutInflater = LayoutInflater.from(this@MainActivity)
            val contentView: View = layoutInflater.inflate(R.layout.calendar, null)
            //对布局里面的控件进行初始化并进行相应的操作
            //初始化PopupWindow
            val popupWindow = PopupWindow(
                contentView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true
            )
            popupWindow.isTouchable = true
            popupWindow.setTouchInterceptor { v, event -> false }
            popupWindow.showAsDropDown(v)
            calendarView2 = contentView.findViewById(R.id.calendarView2) as CalendarView
            calendarView2!!.setOnDateChangeListener(OnDateChangeListener { calendarView2, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar[year, month] = dayOfMonth
                val simpleFormatter = SimpleDateFormat("yyyy-MM-dd")
                val date = simpleFormatter.format(calendar.time)
                //
                date_end!!.setText(date)
                resultSum!!.text = ""
                //显示用户选择的日期
                Toast.makeText(
                    this@MainActivity,
                    year.toString() + "年" + (month + 1) + "月" + dayOfMonth + "日",
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
        btn_edit!!.setOnClickListener {
            val intent = Intent(this@MainActivity, CalendarAndCalories::class.java)
            startActivity(intent)
        }
        btn_query!!.setOnClickListener(View.OnClickListener {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val begin = date_start!!.getText().toString()
            Log.d("my", begin)
            val end = date_end!!.getText().toString()
            Log.d("my", end)
            if (begin == null || begin == "") {
                Toast.makeText(this@MainActivity, "开始日期还没选择", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (end == null || end == "") {
                Toast.makeText(this@MainActivity, "开始日期还没选择", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            var be: Date? = null
            var en: Date? = null
            var sum = 0
            try {
                be = format.parse(begin)
                en = format.parse(end)
                sum = dataParse.checkSumOfCalories(be, en)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            resultSum!!.text = "你共计消耗" + sum + "卡路里"
        })
        val size: Int = (dataParse.getNowCT()?.size  as Int)
        dataParse.sort()
        for (i in 0 until size) {
            val series = BarGraphSeries(
                dataParse.getNowCT()?.get(i)?.calories?.let {
                    DataPoint(
                        dataParse.getNowCT()?.get(i)?.date,
                        it.toDouble()
                    )
                }?.let {
                    arrayOf<DataPoint>(
                        it
                    )
                }
            )

//            series.isDrawValuesOnTop = true

            graphView!!.addSeries(series)
        }
        // set date label formatter
        graphView!!.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this@MainActivity)
        //最多同时显示5个，太多会重叠
        graphView!!.gridLabelRenderer.numHorizontalLabels = 4



        // set manual x bounds to have nice steps
//        graphView!!.viewport.setMinX(dataParse.getNowCT()!![0].date.getTime().toDouble())
//        graphView!!.viewport.setMaxX(dataParse.getNowCT()!![size - 1].date.getTime().toDouble())

//        graphView.getViewport().setYAxisBoundsManual(true);
//        graphView!!.viewport.isXAxisBoundsManual = true

        //水平滚动
        graphView!!.viewport.isScrollable = true
        graphView!!.viewport.setScrollableY(true)


        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
//        graphView!!.gridLabelRenderer.setHumanRounding(false)
    }


    override fun onResume() {
        super.onResume()
        dataParse.readDataFromFile(this)
        val size: Int = (dataParse.getNowCT()?.size  as Int)
        dataParse.sort()
        graphView?.removeAllSeries()
        for (i in 0 until size) {
            val series = BarGraphSeries(
                dataParse.getNowCT()?.get(i)?.calories?.let {
                    DataPoint(
                        dataParse.getNowCT()?.get(i)?.date,
                        it.toDouble()
                    )
                }?.let {
                    arrayOf<DataPoint>(
                        it
                    )
                }
            )

            series.isDrawValuesOnTop = true

            graphView!!.addSeries(series)
        }

    }
}