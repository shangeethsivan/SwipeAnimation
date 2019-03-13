package co.shrappz.swipeanimation

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout


class DragLayout : RelativeLayout {

    private lateinit var mDragHelper: ViewDragHelper
    private lateinit var mDragView: View
    var finalLeft : Int = 0
    var finalTop :Int = 0
    val TAG = "DragLayout"

    constructor(context: Context?):this(context,null){

    }

    constructor(context: Context?,attributes: AttributeSet?):this(context,attributes,0){

    }

    constructor(context: Context?, attributes: AttributeSet?,defStyle:Int): super(context,attributes,defStyle) {
        mDragHelper = ViewDragHelper.create(this,1.0f,DragHelperCallback())
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev!!.action
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel()
            return false
        }
        return mDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDragHelper.processTouchEvent(event!!)
        if(event.action == MotionEvent.ACTION_UP){
            if(mDragHelper.smoothSlideViewTo(mDragView,finalLeft,finalTop)){
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }
        return true
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }


    inner class DragHelperCallback : ViewDragHelper.Callback() {


        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            Log.d(TAG,"onViewPositionChanged")
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
            Log.d(TAG,"onViewCaptured")
        }

        override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {
            Log.d(TAG, "onEdgeTouched")
            super.onEdgeTouched(edgeFlags, pointerId)
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return super.clampViewPositionVertical(child,top,dy)
        }

        override fun onEdgeLock(edgeFlags: Int): Boolean {
            return super.onEdgeLock(edgeFlags)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return super.getViewVerticalDragRange(child)
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return super.getViewHorizontalDragRange(child)
        }

        override fun getOrderedChildIndex(index: Int): Int {
            return super.getOrderedChildIndex(index)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            val leftBound = paddingLeft
            val rightBound = width - (mDragView.findViewById<View>(R.id.round_icon).width + (mDragView.findViewById<View>(R.id.round_icon).layoutParams as (RelativeLayout.LayoutParams)).marginStart)
            val newLeft =Math.min(Math.max(left,leftBound),rightBound)

            var middleValue :Int= width/2
            val halfOfMid :Int= (width*0.1).toInt()

            if(dx<0) {
                middleValue += halfOfMid
                if (newLeft > middleValue) {
                    finalLeft = rightBound
                    finalTop = mDragView.y.toInt()
                } else {
                    finalLeft = leftBound
                    finalTop = mDragView.y.toInt()
                }
            }else{
                middleValue = halfOfMid
                if (newLeft > middleValue) {
                    finalLeft = rightBound
                    finalTop = mDragView.y.toInt()
                } else {
                    finalLeft = leftBound
                    finalTop = mDragView.y.toInt()
                }
            }

            Log.d(TAG,"clampViewPositionHorizontal left: $left dx: $dx  x: ${child.x}")
            Log.d(TAG,"clampViewPositionHorizontal leftbound: $leftBound rightbound: $rightBound newLeft :: $newLeft")

            return newLeft
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
        }


        override fun tryCaptureView(pView: View, p1: Int): Boolean {
            Log.d(TAG,"TryCaptureView $p1")
                mDragView = pView
            return true
        }

    }
}