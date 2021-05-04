package com.example.recordeverdaycalories

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.util.*

class dataParse {


    companion object {

    //增加或修改
    fun addItem(c: CaloriesTimescale) {
        var flag = false
        var position = 0
        for (i in nowCT!!.indices) {
            if (nowCT!![i].date.compareTo(c.date) == 0) {
                flag = true
                position = i
                break
            }
        }
        if (!flag) {
            nowCT!!.add(c)
        } else {
            nowCT!![position].calories = c.calories
        }
    }

    //删1 (根据日期删除元素)
    fun delItem(d: Date?) {
        if (nowCT!!.size == 0 || d == null) {
            return
        }
        for (i in nowCT!!.indices) {
            if (nowCT!![i].date.compareTo(d) == 0) {
                nowCT!!.removeAt(i)
                return
            }
        }
    }

    //改
    fun changeItem(c: CaloriesTimescale?) {}



        private var nowCT: MutableList<CaloriesTimescale>? = ArrayList()

        //从文件record.json读取json出来
        fun readDataFromFile(context: Context): List<CaloriesTimescale>? {
            val gson = Gson()
            var `in`: FileInputStream? = null
            var reader: BufferedReader? = null
            val content = StringBuilder()
            try {
                `in` = context.openFileInput("record.json")
                reader = BufferedReader(InputStreamReader(`in`))
                var line: String? = ""
                while (reader.readLine().also { line = it } != null) {
                    content.append(line)
                }

                //将json字符串解析成List<CalendarAndCalories>类型的成员变量
                nowCT = gson.fromJson(
                    content.toString(),
                    object : TypeToken<List<CaloriesTimescale?>?>() {}.type
                )
                sort()
                reader.close()
                `in`.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return nowCT
        }

        //将更新的json写回record.txt中
        fun writeDataToFile(context: Context, string: String?) {
            var string = string
            var out: FileOutputStream? = null
            var Writer: BufferedWriter? = null
            if (string == null) {
                string = ""
            }
            try {
                out = context.openFileOutput("record.json", Context.MODE_PRIVATE)
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

        fun getNowCT(): List<CaloriesTimescale>? {
            return nowCT
        }

        fun sort() {
            Collections.sort(nowCT, object : Comparator<CaloriesTimescale> {
                override fun compare(o1: CaloriesTimescale, o2: CaloriesTimescale): Int {
                    if (null == o1.date) {
                        return -1
                    }
                    return if (null == o2.date) {
                        1
                    } else o1.date.compareTo(o2.date)
                }
            })
        }

        //删2  (根据index删除元素)
        fun delItem(positon: Int) {
            if (nowCT == null || nowCT!!.size < positon) {
                return
            }
            nowCT!!.removeAt(positon)
        }

        //查
        fun checkSumOfCalories(start: Date?, end: Date?): Int {
            if (nowCT!!.size == 0) {
                return 0
            } else if (start == null || end == null) {
                Log.d("my", "checkSumOfCalories: null")
                return 0
            } else if (start.compareTo(end) > 0) {
                return 0
            }
            var NumOfCalories = 0
            for (i in nowCT!!.indices) {
                //在[start,end]这个区间
                if (nowCT!![i].date.compareTo(start) >= 0 && nowCT!![i].date.compareTo(end) <= 0) {
                    NumOfCalories = NumOfCalories + nowCT!![i].calories
                }
            }
            return NumOfCalories
        }
    }
}