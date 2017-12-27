package com.wjj.geekfolder.main

import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.wjj.geekfolder.R

/**
 *
 * @author wujiajun
 * @date 17-12-21
 */

class GeekFolderMainUi {

    private lateinit var mActivity: GeekFolderActivity
    private lateinit var mController: GeekFolderMainController
    private lateinit var mRecycleView: RecyclerView

    constructor (activity: GeekFolderActivity, controller: GeekFolderMainController){
        mActivity = activity
        mController = controller

        initView()
    }

    private fun initView(){
        val root = mActivity.window.decorView.findViewById<View>(android.R.id.content) as FrameLayout
        LayoutInflater.from(mActivity).inflate(R.layout.activity_geek_folder, root)

        val toolbar = root.findViewById<View>(R.id.toolbar) as Toolbar
        mActivity.setSupportActionBar(toolbar)
        val fab = root.findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Snackbar show", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        mRecycleView = root.findViewById<View>(R.id.rv_main_activity) as RecyclerView
        // 总是忘记
        mRecycleView.layoutManager = LinearLayoutManager(mActivity)
        var recycleViewAdapter = MainRecycleViewAdapter(mActivity)
        mRecycleView.adapter = recycleViewAdapter

    }
}
