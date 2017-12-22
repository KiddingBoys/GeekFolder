package com.wjj.geekfolder.main

/**
 * Created by wujiajun on 17-12-22.
 */
class MainRecycleViewBean {
    /** 静态变量或单例 */
    companion object {
        val TYPE_CLASSIFY_VIEW = 1;
    }


    var mType: Int
    constructor(type: Int){
        mType = type
    }
}