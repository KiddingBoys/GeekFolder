package com.wjj.geekfolder.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wjj.geekfolder.BuildConfig
import com.wjj.geekfolder.views.HomeClassifyView

/**
 * Created by wujiajun on 17-12-21.
 */

class MainRecycleViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var TAG = "MainRecycleViewAdapter"
    private var mContext: Context
    private var mLayoutInflater: LayoutInflater
    /** views */
    private var mHomeClassifyView: HomeClassifyView

    /** beans */
    private var mHomeClassifyBean = MainRecycleViewBean(MainRecycleViewBean.TYPE_CLASSIFY_VIEW)

    private var mDataList = ArrayList<MainRecycleViewBean>()


    constructor(context: Context){
        mContext = context
        mLayoutInflater = LayoutInflater.from(context)
        /** init views */
        mHomeClassifyView = HomeClassifyView(context)

        refreshDataList()
    }

    /** refresh DataList*/
    private fun refreshDataList(){
        if(mDataList == null){
            return
        }
        var isNeedRefresh = false

        if(!mDataList.contains(mHomeClassifyBean)){
            mDataList.add(mHomeClassifyBean)
            isNeedRefresh = true
        }

        // judge if the list need refresh
        if(isNeedRefresh){
            if(BuildConfig.DEBUG){
                Log.d(TAG,"refreshDataList notifyDataSetChanged")
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {

        var viewHolder = when(viewType){
            MainRecycleViewBean.TYPE_CLASSIFY_VIEW -> HomeRecycleViewHolder(mHomeClassifyView)
            else -> null
        }
        if(BuildConfig.DEBUG){
            Log.d(TAG,"onCreateViewHolder viewType = " + viewType)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        if(mDataList == null || mDataList.get(position) == null){
            return 0
        }
        return mDataList.get(position).mType
    }


    class HomeRecycleViewHolder : RecyclerView.ViewHolder{
        constructor(view:View):super(view){
        }
    }
}
