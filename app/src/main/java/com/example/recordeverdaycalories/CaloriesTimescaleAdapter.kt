package com.example.recordeverdaycalories

import android.content.Context
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

//class CaloriesTimescaleAdapter(private val caloriesTimescaleList: List<CaloriesTimescale>) :
//    RecyclerView.Adapter<CaloriesTimescaleAdapter.ViewHolder>() {
//    var contextMenuPosition = 0
//    private var mContext: Context? = null
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        if (mContext != null) {
//            mContext = parent.context
//        }
//        val view: View = LayoutInflater.from(parent.context)
//            .inflate(R.layout.test_item, parent, false)
//        val holder: ViewHolder = ViewHolder(view)
//        //单击->返回元素值
//        holder.itemView.setOnClickListener { v ->
//            val position = holder.adapterPosition
//            val s = "" + dataParse.getNowCT()!![position].calories
//            Toast.makeText(v.context, s, Toast.LENGTH_SHORT).show()
//        }
//
//        //长按->弹出菜单
//        holder.itemView.setOnLongClickListener { v ->
//            val position = holder.adapterPosition
//            val s = "" + dataParse.getNowCT()!![position].calories
//            Toast.makeText(v.context, s, Toast.LENGTH_SHORT).show()
//            Log.d("my", "onClick: $s")
//            false
//        }
//        return holder
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val ct = caloriesTimescaleList[position]
//        holder.text1.text = "" + dateFormat.format(ct.date)
//        Log.d("holderID", "onBindViewHolder: " + ct.date)
//        holder.text2.text = "" + ct.calories
//        Log.d("holderID2", "onBindViewHolder: " + ct.calories)
//        holder.itemView.setOnLongClickListener {
//            contextMenuPosition = holder.layoutPosition
//            false
//        }
//
//    }
//
//    override fun onViewRecycled(holder: ViewHolder) {
//        holder.itemView.setOnLongClickListener(null)
//        super.onViewRecycled(holder)
//    }
//
//    override fun getItemCount(): Int {
//        Log.d("t.size()", "getItemCount: " + caloriesTimescaleList.size)
//        return caloriesTimescaleList.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
//        OnCreateContextMenuListener {
//        //保存子项最外层布局的view
//        lateinit var itemView1: View
//        var text1: TextView
//        var text2: TextView
//        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
//            menu.setHeaderTitle("记录" + (contextMenuPosition + 1))
//            menu.add(ContextMenu.NONE, 0, ContextMenu.NONE, "查看")
//            menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, "删除")
//            menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, "修改")
//        }
//
//        init {
//            itemView1 = itemView
//            text1 = itemView1.findViewById<View>(R.id.text1) as TextView
//            text2 = itemView1.findViewById<View>(R.id.text2) as TextView
//            itemView1.setOnCreateContextMenuListener(this)
//        }
//    }
//}


class CaloriesTimescaleAdapter(private val caloriesTimescaleList: List<CaloriesTimescale?>?) :
    RecyclerView.Adapter<CaloriesTimescaleAdapter.ViewHolder>() {
    var contextMenuPosition = 0
    private var mContext: Context? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CaloriesTimescaleAdapter.ViewHolder {
        if (mContext != null) {
            mContext = parent.context
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_item, parent, false)
        val holder: CaloriesTimescaleAdapter.ViewHolder =
            ViewHolder(view)
        //单击->返回元素值
        holder.itemView.setOnClickListener(View.OnClickListener { v ->
            val position: Int = holder.getAdapterPosition()
            val s = "" + dataParse.getNowCT()!![position].calories
            Toast.makeText(v.context, s, Toast.LENGTH_SHORT).show()
        })

        //长按->弹出菜单
        holder.itemView.setOnLongClickListener(OnLongClickListener { v ->
            val position: Int = holder.getAdapterPosition()
            val s = "" + dataParse.getNowCT()!![position].calories
            Toast.makeText(v.context, s, Toast.LENGTH_SHORT).show()
            Log.d("my", "onClick: $s")
            false
        })
        return holder
    }

    override fun onBindViewHolder(
        holder: CaloriesTimescaleAdapter.ViewHolder,
        position: Int
    ) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val ct = caloriesTimescaleList?.get(position)
        if (ct != null) {
            holder.text1.setText("" + dateFormat.format(ct.date))
        }
        if (ct != null) {
            Log.d("holderID", "onBindViewHolder: " + ct.date)
        }
        if (ct != null) {
            holder.text2.setText("" + ct.calories)
        }
        if (ct != null) {
            Log.d("holderID2", "onBindViewHolder: " + ct.calories)
        }
        holder.itemView.setOnLongClickListener(OnLongClickListener {
            contextMenuPosition = holder.getLayoutPosition()
            false
        })
    }

    override fun onViewRecycled(holder:CaloriesTimescaleAdapter.ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        if (caloriesTimescaleList != null) {
            Log.d("t.size()", "getItemCount: " + caloriesTimescaleList.size)
            return caloriesTimescaleList.size
        }
        else{
            return 0;
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnCreateContextMenuListener {
        //保存子项最外层布局的view
        var itemView1: View
        var text1: TextView
        var text2: TextView
        override fun onCreateContextMenu(menu: ContextMenu?, v: View, menuInfo: ContextMenuInfo?) {
            if (menu != null) {
                menu.setHeaderTitle("记录" + (contextMenuPosition + 1))
            }
            if (menu != null) {
                menu.add(ContextMenu.NONE, 0, ContextMenu.NONE, "查看")
            }
            if (menu != null) {
                menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, "删除")
            }
            if (menu != null) {
                menu.add(ContextMenu.NONE, 2, ContextMenu.NONE, "修改")
            }
        }

        init {
            this.itemView1 = itemView
            text1 = itemView1.findViewById<View>(R.id.text1) as TextView
            text2 = itemView1.findViewById<View>(R.id.text2) as TextView
            itemView1.setOnCreateContextMenuListener(this)
        }
    }
}