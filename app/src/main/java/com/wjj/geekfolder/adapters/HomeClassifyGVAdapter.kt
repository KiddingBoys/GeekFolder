package com.wjj.geekfolder.adapters

import android.content.Context
import android.database.DataSetObservable
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.wjj.geekfolder.BuildConfig
import com.wjj.geekfolder.R

/**
 * Created by wujiajun on 17-12-22.
 */
class HomeClassifyGVAdapter : ListAdapter {

    private val TAG = "HomeClassifyGVAdapter"
    private var mContext: Context
    private var mDataList: ArrayList<HashMap<String, Object>>
    private var mDataSetObservable = DataSetObservable()

    constructor(context: Context, dataList: ArrayList<HashMap<String, Object>>) {
        mContext = context
        mDataList = dataList

    }

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEnabled(p0: Int): Boolean {
        return true
    }

    override fun isEmpty(): Boolean {
        return mDataList == null || mDataList.size == 0
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {
        mDataSetObservable.registerObserver(p0)
    }

    override fun hasStableIds(): Boolean {
        //返回false的话 每次调用notifyDataSetChanged方法时 adapter就会判断getItemId,只调用那些Item发生变化的getView方法
        return true
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {
        mDataSetObservable.unregisterObserver(p0)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ClassifyGVViewHolder
        var itemView: View
        if (convertView == null) {
            holder = ClassifyGVViewHolder()
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_classify, parent, false)
            holder.titleView = itemView.findViewById<View>(R.id.tv_item_classify_name) as TextView
            holder.iconView = itemView.findViewById<View>(R.id.iv_item_classify_icon) as ImageView
            itemView.tag = holder
        } else {
            itemView = convertView
            holder = itemView.tag as ClassifyGVViewHolder
        }

        holder.titleView.text = mDataList[position].get("ItemText").toString()
        holder.iconView.setImageResource(mDataList[position].getValue("ItemImage") as Int)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getView position = " + position)
        }

        return itemView
    }

    override fun getItemViewType(p0: Int): Int {
        return 0
    }

    override fun getItem(p0: Int): Any {
        return mDataList.get(p0)
    }

    override fun getViewTypeCount(): Int {
        return 1

    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    class ClassifyGVViewHolder {
        lateinit var titleView: TextView
        lateinit var iconView: ImageView
    }

}