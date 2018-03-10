package com.example.zabst.pagertest

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by zabst on 09.03.2018.
 */
open class PageAdapterTest(private var map: HashMap<Int, Boolean>, private var activity: MainActivity, private var colorMap: HashMap<Int, Int>): PagerAdapter() {

    private val TAG: String = "SWIPE_TAG"

    private var downX: Float = 0.0f
    private var downY: Float = 0.0f
    private var upX: Float = 0.0f
    private var upY: Float = 0.0f

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return map.size
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val layout: ConstraintLayout = LayoutInflater.from(container!!.context).inflate(R.layout.fragment_child, null) as ConstraintLayout
        val text: TextView? = layout.findViewById(R.id.texts)

        val realPosition: Int = activity.getCurrentItem()

        container.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = motionEvent.x
                    downY = motionEvent.y
                    false
                }
                MotionEvent.ACTION_UP -> {
                    upX = motionEvent.x
                    upY = motionEvent.y

                    val deltaX: Float = downX - upX
                    val deltaY: Float = downY - upY

                    if (Math.abs(deltaX) > activity.getScaleX()) {
                        if (map.filter { !it.value }.size < 2)
                            alertPager()

                        if (deltaX > 0) {
                            Log.d(TAG, "swipe right: $realPosition")
                            var iter = realPosition + 1 //next pos
                            if (iter == count)
                                iter = 0
                            while (true){
                                if (!map[iter]!!) {
                                    activity.setPosition(iter)
                                    activity.chooseColor(iter, colorMap)
                                    break
                                } else {
                                    if (iter == count-1)
                                        iter = 0
                                    else
                                        iter++
                                }
                            }
                            return@setOnTouchListener true
                        }

                        if (deltaX < 0) {
                            Log.d(TAG, "swipe left: $realPosition")
                            var iter = realPosition - 1 //prev pos
                            if (iter < 0)
                                iter = count - 1
                            while (true){
                                if (!map[iter]!!) {
                                    activity.setPosition(iter)
                                    activity.chooseColor(iter, colorMap)
                                    break
                                } else {
                                    if (iter == 0)
                                        iter = count - 1
                                    else
                                        iter--
                                }
                            }
                            return@setOnTouchListener true
                        }
                    }

                    if (Math.abs(deltaY) > activity.getScaleY()) {
                        if (deltaY > 0) {
                            Log.d(TAG, "swipe top: $realPosition")
                            map[realPosition] = true
                            colorMap[realPosition] = 1
                            activity.chooseColor(realPosition, colorMap)
                            //activity.updateAdapter()
                            return@setOnTouchListener true
                        }

                        if (deltaY < 0) {
                            Log.d(TAG, "swipe bottom: $realPosition")
                            map[realPosition] = false
                            colorMap[realPosition] = 2
                            activity.chooseColor(realPosition, colorMap)
                            //activity.updateAdapter()
                            return@setOnTouchListener true
                        }
                    } else {
                        Log.d(TAG, "This is click")
                        activity.showMessage("This is click")
                    }

                    true
                }
                else -> {
                    false
                }
            }
        }
        var pos = realPosition
        pos+=1

        activity.setTitleCount(getCurrentRealPosition(pos, map), map.filter { !it.value }.size, map.filter { it.value }.size)

        text!!.text = "Position ${position+1}"
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        val layout: ConstraintLayout = `object` as ConstraintLayout
        container!!.removeView(layout)
    }

    fun getCurrentRealPosition(pos: Int, currentMap: HashMap<Int, Boolean>): Int {

        var iter = 0
        var res = 0

        if (currentMap.filter { it.value }.isEmpty())
            return pos

        // sort by key
        val otherMap = currentMap.filter { !it.value }.toList().sortedBy {(k,_ ) -> k}.toMap()

        for (entry in otherMap){
            if (entry.key == pos-1)
                res = iter
            iter++
        }

        return res+1
    }

    fun alertPager() {
        val alert: AlertDialog = AlertDialog.Builder(activity)
                .setTitle("Page count less then two")
                .setMessage("Some text....")
                .setPositiveButton("OK", {
                    // go to another activity
                    _, _ -> activity.finish()

                })
                .create()

        alert.show()
    }

}
