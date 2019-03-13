package co.shrappz.swipeanimation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.widget.ViewDragHelper
import android.util.Log
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnGestureListener {

    val TAG = "MainActivity"

    private lateinit var mDetector: GestureDetectorCompat
    private var isShrinked: Boolean = false

    var mCurrentView : View? = null

    private lateinit var dragger:ViewDragHelper

    private val TOUCH_SLOP_SENSITIVITY = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDetector = GestureDetectorCompat(this, this)

        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        layout_1.startAnimation(animation)


        layout_1.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                mCurrentView = v
                mDetector.onTouchEvent(event)
                return true
            }
        })

        layout_2.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                mCurrentView = v
                mDetector.onTouchEvent(event)
                return true
            }
        })
//
//        var callback = DragHelperCallback()
//
//        dragger = ViewDragHelper.create(layout_3,TOUCH_SLOP_SENSITIVITY,callback)
//        dragger.setEdgeTrackingEnabled(ViewDragHelper.DIRECTION_HORIZONTAL)
//        dragger.minVelocity = 4000 * resources.displayMetrics.density
//        callback.setDragger(dragger)
//        callback.setView(layout_3)
//
//        layout_3.setOnTouchListener(object :View.OnTouchListener{
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                if (event != null) {
//                    dragger.processTouchEvent(event)
//                }
//                return true
//            }
//        })

    }


    fun expand() {
        Log.d(TAG, "::: Expanding")
        isShrinked = false
        val animation: Animation = AnimationUtils.loadAnimation(this, if(mCurrentView?.id == R.id.layout_1) R.anim.slide_in_from_right else R.anim.slide_in_from_right_s)
        mCurrentView?.startAnimation(animation)
    }

    fun shrink() {
        Log.d(TAG, "::: Expanding")
        isShrinked = true
        val animation: Animation = AnimationUtils.loadAnimation(this, if(mCurrentView?.id == R.id.layout_1) R.anim.slide_out_to_right else R.anim.slide_out_to_right_s)
        mCurrentView?.startAnimation(animation)
    }


    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        if (isShrinked) {
            expand()
        } else {
            shrink()
        }
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        Log.d(TAG, "e1 x: " + e1?.x + " e1 y: " + e1?.y + " e2 x: " + e2?.x + " e2 y: " + e2?.y)
        Log.d(TAG, "VelocityX: " + velocityX + " VelocityY: " + velocityY)

        if (e1 != null && e2 != null) {
            if (e1.x < e2.x) {
                if (!isShrinked)
                    shrink()
            } else if (e1.x > e2.x) {
                if (isShrinked)
                    expand()
            }
        }
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Log.d(TAG, "Scroll --> e1 x: " + e1?.x + " e1 y: " + e1?.y + " e2 x: " + e2?.x + " e2 y: " + e2?.y)

        if (e1 != null && e2 != null) {
            // only initiating shrink/expand if there is a good amount of scroll
            if (Math.abs(e1.x - e2.x) > 100) {

                if (e1.x < e2.x) {
                    if (!isShrinked) {
                        shrink()
                    }
                } else if (e1.x > e2.x) {
                    if (isShrinked) {
                        expand()
                    }
                }
            }
        }
        return true
    }

    override fun onLongPress(e: MotionEvent?) {

    }



}
