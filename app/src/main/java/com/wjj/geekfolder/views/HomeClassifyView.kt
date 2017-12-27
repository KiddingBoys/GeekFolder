package com.wjj.geekfolder.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.GridView
import com.wjj.geekfolder.R
import com.wjj.geekfolder.adapters.HomeClassifyGVAdapter
import java.util.*


/**
 * Created by wujiajun on 17-12-22.
 */
class HomeClassifyView: FrameLayout {

    var mClassifyGV: GridView
    var mClassifyGVAdapter: HomeClassifyGVAdapter

    constructor(context: Context):super(context){
        LayoutInflater.from(context).inflate(R.layout.view_home_classify, this)
        mClassifyGV = findViewById<View>(R.id.gv_home_classify_view) as GridView
        //生成动态数组，并且转入数据
        val dataList = getDataList(context)
        mClassifyGVAdapter = HomeClassifyGVAdapter(context, dataList)
        mClassifyGV.adapter = mClassifyGVAdapter
        mClassifyGVAdapter.notifyDataSetChanged()
    }

    private val IMAGERES = intArrayOf(R.mipmap.icon_picture, R.mipmap.icon_music, R.mipmap.icon_media, R.mipmap.icon_file, R.mipmap.icon_download, R.mipmap.icon_apk)
    private fun getDataList(context:Context): ArrayList<HashMap<String, Object>> {
        var titles = context.resources.getStringArray(R.array.home_classify_title)
        val dataList = ArrayList<HashMap<String, Object>>()
        for (i in 0 until IMAGERES.size) {
            val map = HashMap<String, Object>()
            map.put("ItemText", titles[i] as Object)//按序号做ItemText
            map.put("ItemImage", IMAGERES[i] as Object)//添加图像资源的ID
            dataList.add(map)
        }
        return dataList
    }
}