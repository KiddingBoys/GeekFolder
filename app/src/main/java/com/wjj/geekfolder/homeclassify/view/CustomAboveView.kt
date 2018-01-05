package com.wjj.geekfolder.homeclassify.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.wjj.geekfolder.R
import com.wjj.geekfolder.homeclassify.model.DargChildInfo
import com.wjj.geekfolder.homeclassify.model.DragIconInfo

import java.util.ArrayList


class CustomAboveView(private val mContext: Context, private val mCustomGroup: CustomGroup) : LinearLayout(mContext, null) , CustomGridView.CustomChildClickListener{

    var iconInfoList = ArrayList<DragIconInfo>()
    private var mItemViewClickListener: ItemViewClickListener? = null
    private val verticalViewWidth = 1
    var gridViewClickListener: CustomAboveViewClickListener? = null
    var firstEvent: MotionEvent? = null


    private var mChildClickListener: CustomGridView.CustomChildClickListener? = null

    interface CustomAboveViewClickListener {

        fun onSingleClicked(iconInfo: DragIconInfo)

        fun onChildClicked(childInfo: DargChildInfo)
    }


    init {
        orientation = LinearLayout.VERTICAL
        initData()
    }

    private fun initData() {
        mChildClickListener = this
    }

    override fun onChildClicked(chilidInfo: DargChildInfo) {
        if (gridViewClickListener != null) {
            gridViewClickListener!!.onChildClicked(chilidInfo)
        }
    }

    fun refreshIconInfoList(iconInfoList: ArrayList<DragIconInfo>) {
        this.iconInfoList.clear()
        this.iconInfoList.addAll(iconInfoList)
        refreshViewUI()
    }

    private fun refreshViewUI() {
        removeAllViews()
        val rowNum = iconInfoList.size / CustomGroup.COLUMNUM + if (iconInfoList.size % CustomGroup.COLUMNUM > 0) 1 else 0
        val rowParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val verticalParams = LinearLayout.LayoutParams(verticalViewWidth, LinearLayout.LayoutParams.FILL_PARENT)
        val horizontalParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, verticalViewWidth)
        for (rowIndex in 0 until rowNum) {
            val rowView = View.inflate(mContext, R.layout.gridview_above_rowview, null)

            val llRowContainer = rowView.findViewById<View>(R.id.gridview_rowcontainer_ll) as LinearLayout
            val ivOpenFlag = rowView.findViewById<View>(R.id.gridview_rowopenflag_iv) as ImageView
            val llBtm = rowView.findViewById<View>(R.id.gridview_rowbtm_ll) as LinearLayout
            val gridViewNoScroll = rowView.findViewById<View>(R.id.gridview_child_gridview) as CustomGridView
            if (mChildClickListener != null) {
                gridViewNoScroll.childClickListener = mChildClickListener
            }
            gridViewNoScroll.setParentView(llBtm)
            val itemParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            itemParam.weight = 1.0f
            val itemClickLitener = ItemViewClickListener(llBtm, ivOpenFlag, object : ItemViewClickInterface {

                override fun shoudInteruptViewAnimtion(listener: ItemViewClickListener, position: Int): Boolean {
                    var isInterupt = false
                    mCustomGroup.clearEditDragView()
                    if (mItemViewClickListener != null && mItemViewClickListener != listener) {
                        mItemViewClickListener!!.closeExpandView()
                    }
                    mItemViewClickListener = listener
                    val iconInfo = iconInfoList[position]
                    val childList = iconInfo.childList
                    if (childList.size > 0) {
                        gridViewNoScroll.refreshDataSet(childList)
                    } else {
                        setViewCollaps()
                        isInterupt = true
                        if (gridViewClickListener != null) {
                            gridViewClickListener!!.onSingleClicked(iconInfo)
                        }
                    }
                    return isInterupt
                }

                override fun viewUpdateData(position: Int) {
                    gridViewNoScroll.notifyDataSetChange(true)
                }
            })
            for (columnIndex in 0 until CustomGroup.COLUMNUM) {
                val itemView = View.inflate(mContext, R.layout.gridview_above_itemview, null)
                val ivIcon = itemView.findViewById<View>(R.id.icon_iv) as ImageView
                val tvName = itemView.findViewById<View>(R.id.name_tv) as TextView
                val itemInfoIndex = rowIndex * CustomGroup.COLUMNUM + columnIndex
                if (itemInfoIndex > iconInfoList.size - 1) {
                    itemView.visibility = View.INVISIBLE
                } else {
                    val iconInfo = iconInfoList[itemInfoIndex]
                    ivIcon.setImageResource(iconInfo.resIconId)
                    tvName.setText(iconInfo.name)
                    itemView.id = itemInfoIndex
                    itemView.tag = itemInfoIndex

                    itemView.setOnClickListener(itemClickLitener)
                    itemView.setOnLongClickListener { v ->
                        if (iconInfo.id !== MORE) {
                            val position = v.tag as Int
                            mCustomGroup.setEditModel(true, position)
                        }
                        true
                    }
                }

                llRowContainer.addView(itemView, itemParam)
                val view = View(mContext)
                view.setBackgroundResource(R.color.gap_line)
                llRowContainer.addView(view, verticalParams)
            }
            val view = View(mContext)
            view.setBackgroundResource(R.color.gap_line)
            addView(view, horizontalParams)
            addView(rowView, rowParam)
            if (rowIndex == rowNum - 1) {
                val btmView = View(mContext)
                btmView.setBackgroundResource(R.color.gap_line)
                addView(btmView, horizontalParams)
            }

        }
    }

    fun setViewCollaps() {
        if (mItemViewClickListener != null) {
            mItemViewClickListener!!.closeExpandView()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        this.firstEvent = ev
        if (mCustomGroup.isEditModel) {
            mCustomGroup.sendEventBehind(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    interface ItemViewClickInterface {
        fun shoudInteruptViewAnimtion(animUtil: ItemViewClickListener, position: Int): Boolean

        fun viewUpdateData(position: Int)
    }

    inner class ItemViewClickListener(val contentView: View, private val mViewFlag: View, private val animationListener: ItemViewClickInterface?) : View.OnClickListener {
        private val INVALID_ID = -1000
        private var mLastViewID = INVALID_ID

        private var startX: Int = 0
        private var viewFlagWidth: Int = 0
        private var itemViewWidth: Int = 0

        override fun onClick(view: View) {
            val viewId = view.id
            var isTheSameView = false
            if (animationListener != null) {
                if (animationListener.shoudInteruptViewAnimtion(this, viewId)) {
                    return
                }
            }
            if (mLastViewID == viewId) {
                isTheSameView = true
            } else {
                mViewFlag.visibility = View.VISIBLE
                viewFlagWidth = getViewFlagWidth()
                itemViewWidth = view.width
                val endX = view.left + itemViewWidth / 2 - viewFlagWidth / 2
                if (mLastViewID == INVALID_ID) {
                    startX = endX
                    xAxismoveAnim(mViewFlag, startX, endX)
                } else {
                    xAxismoveAnim(mViewFlag, startX, endX)
                }
                startX = endX
            }
            val isVisible = contentView.visibility == View.VISIBLE
            if (isVisible) {
                if (isTheSameView) {
                    animateCollapsing(contentView)
                } else {
                    animationListener?.viewUpdateData(viewId)
                }
            } else {
                if (isTheSameView) {
                    mViewFlag.visibility = View.VISIBLE
                    xAxismoveAnim(mViewFlag, startX, startX)
                }
                animateExpanding(contentView)
            }
            mLastViewID = viewId
        }

        private fun getViewFlagWidth(): Int {
            var viewWidth = mViewFlag.width
            if (viewWidth == 0) {
                val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                mViewFlag.measure(widthSpec, heightSpec)
                viewWidth = mViewFlag.measuredWidth
            }
            return viewWidth
        }

        fun xAxismoveAnim(v: View, startX: Int, toX: Int) {
            moveAnim(v, startX, toX, 0, 0, 200)
        }

        private fun moveAnim(v: View, startX: Int, toX: Int, startY: Int, toY: Int, during: Long) {
            val anim = TranslateAnimation(startX.toFloat(), toX.toFloat(), startY.toFloat(), toY.toFloat())
            anim.duration = during
            anim.fillAfter = true
            v.startAnimation(anim)
        }

        fun closeExpandView() {
            val isVisible = contentView.visibility == View.VISIBLE
            if (isVisible) {
                animateCollapsing(contentView)
            }
        }

        fun animateCollapsing(view: View) {
            val origHeight = view.height

            val animator = createHeightAnimator(view, origHeight, 0)
            animator.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animator: Animator) {
                    view.visibility = View.GONE
                    mViewFlag.clearAnimation()
                    mViewFlag.visibility = View.GONE
                }
            })
            animator.start()
        }

        fun animateExpanding(view: View) {
            view.visibility = View.VISIBLE
            val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(widthSpec, heightSpec)
            val animator = createHeightAnimator(view, 0, view.measuredHeight)
            animator.start()
        }

        fun createHeightAnimator(view: View, start: Int, end: Int): ValueAnimator {
            val animator = ValueAnimator.ofInt(start, end)
            animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {

                override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                    val value = valueAnimator.getAnimatedValue() as Int

                    val layoutParams = view.layoutParams
                    layoutParams.height = value
                    view.layoutParams = layoutParams
                }
            })
            return animator
        }

    }

    companion object {
        val MORE = 99999
    }
}
