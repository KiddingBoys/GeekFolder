package com.wjj.geekfolder.utils

import android.content.Context

/**
 * Created by wujiajun on 17-12-25.
 */
class UIUtils {

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}