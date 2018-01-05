package com.wjj.geekfolder.homeclassify.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wjj.geekfolder.R
import com.wjj.geekfolder.homeclassify.model.DargChildInfo
import com.wjj.geekfolder.utils.UIUtils

import java.util.ArrayList

/**
 *
 * 类: CustomGridView
 *
 *
 * 描述: TODO
 *
 *
 * 作者: wedcel wedcel@gmail.com
 *
 *
 * 时间: 2015年8月25日 下午7:07:44
 *
 *
 */
class CustomGridView : LinearLayout {

    private var mContext: Context? = null
    private val mPlayList = ArrayList<DargChildInfo>()
    private var viewHeight: Int = 0
    private var viewWidth: Int = 0
    private var mParentView: LinearLayout? = null
    private var rowNum: Int = 0
    private var verticalViewWidth: Int = 0
    var childClickListener: CustomChildClickListener? = null

    interface CustomChildClickListener {
        fun onChildClicked(chilidInfo: DargChildInfo)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    private fun initView(context: Context) {
        this.mContext = context
        verticalViewWidth = UIUtils.dip2px(mContext!!, 1F)
        val root = View.inflate(mContext, R.layout.gridview_child_layoutview, null)
        val textView = root.findViewById<View>(R.id.gridview_child_name_tv) as TextView
        val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        textView.measure(widthSpec, heightSpec)
        viewHeight = textView.measuredHeight
        viewWidth = (mContext!!.resources.displayMetrics.widthPixels - UIUtils.dip2px(mContext!!, 2F)) / CustomGroup.COLUMNUM
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    fun refreshDataSet(playList: ArrayList<DargChildInfo>) {
        mPlayList.clear()
        mPlayList.addAll(playList)
        notifyDataSetChange(false)
    }

    fun notifyDataSetChange(needAnim: Boolean) {
        removeAllViews()
        rowNum = mPlayList.size / CustomGroup.COLUMNUM + if (mPlayList.size % CustomGroup.COLUMNUM > 0) 1 else 0
        val rowParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val verticalParams = LinearLayout.LayoutParams(verticalViewWidth, LinearLayout.LayoutParams.FILL_PARENT)
        val horizontalParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, verticalViewWidth)
        for (rowIndex in 0 until rowNum) {
            val llContainer = LinearLayout(mContext)
            llContainer.orientation = LinearLayout.HORIZONTAL
            val itemParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemParam.width = viewWidth
            for (columnIndex in 0 until CustomGroup.COLUMNUM) {
                val itemInfoIndex = rowIndex * CustomGroup.COLUMNUM + columnIndex
                var isValidateView = true
                if (itemInfoIndex >= mPlayList.size) {
                    isValidateView = false
                }
                val root = View.inflate(mContext, R.layout.gridview_child_layoutview, null)
                val textView = root.findViewById<View>(R.id.gridview_child_name_tv) as TextView
                if (isValidateView) {
                    val tempChilid = mPlayList[itemInfoIndex]
                    textView.setText(tempChilid.name)
                    textView.setOnClickListener {
                        if (childClickListener != null) {
                            childClickListener!!.onChildClicked(tempChilid)
                        }
                    }
                }
                llContainer.addView(root, itemParam)
                if (columnIndex != CustomGroup.COLUMNUM - 1) {
                    val view = View(mContext)
                    view.setBackgroundResource(R.drawable.ver_line)
                    llContainer.addView(view, verticalParams)
                }
            }
            addView(llContainer, rowParam)
            val view = View(mContext)
            view.setBackgroundResource(R.drawable.hor_line)
            addView(view, horizontalParams)
            Log.e("animator", "" + height + "--" + rowNum * viewHeight)
            if (needAnim) {
                createHeightAnimator(mParentView, this@CustomGridView.height, rowNum * viewHeight)
            }
        }
    }

    /**
     * 方法: createHeightAnimator
     *
     *
     * 描述: TODO
     *
     *
     * 参数: @param view
     * 参数: @param start
     * 参数: @param end
     *
     *
     * 返回: void
     *
     *
     */
    fun createHeightAnimator(view: View?, start: Int, end: Int) {
        val animator = ValueAnimator.ofInt(start, end)
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {

            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val value = valueAnimator.getAnimatedValue() as Int

                val layoutParams = view!!.layoutParams
                layoutParams.height = value
                view.layoutParams = layoutParams
            }
        })
        animator.setDuration(200)
        animator.start()
    }

    /**
     * 方法: setParentView
     *
     *
     * 描述: TODO
     *
     *
     * 参数: @param llBtm
     *
     *
     * 返回: void
     *
     *
     */
    fun setParentView(llBtm: LinearLayout) {
        this.mParentView = llBtm
    }
}
