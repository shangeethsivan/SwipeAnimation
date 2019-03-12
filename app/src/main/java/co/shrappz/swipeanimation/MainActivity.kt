package co.shrappz.swipeanimation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDetector = GestureDetectorCompat(this, this)

        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        constraint_layout.startAnimation(animation)

        constraint_layout.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                mDetector.onTouchEvent(event)
                return true
            }
        })
    }


    fun expand() {
        Log.d(TAG, "::: Expanding")
        isShrinked = false
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        constraint_layout.startAnimation(animation)
    }

    fun shrink() {
        Log.d(TAG, "::: Expanding")
        isShrinked = true
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right)
        constraint_layout.startAnimation(animation)
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
            if (Math.abs(e1.x - e2.x) > 200) {

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
