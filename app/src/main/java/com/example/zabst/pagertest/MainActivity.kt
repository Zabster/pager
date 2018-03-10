package com.example.zabst.pagertest

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tmall.ultraviewpager.UltraViewPager

class MainActivity : AppCompatActivity() {

    private var ultraViewPager: UltraViewPager? = null
    private var pageAdapterTest: PageAdapterTest? = null

    private var textCount: TextView? = null
    private var textLearnCount: TextView? = null

    private var topImage: ImageView? = null
    private var botImage: ImageView? = null

    private var map: HashMap<Int, Boolean>? = null
    private var colorMap: HashMap<Int, Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        map = HashMap()
        colorMap = HashMap()
        for (i: Int in 0..5) {
            map!![i] = false
            colorMap!![i] = 0
        }

        topImage = findViewById(R.id.topView)
        botImage = findViewById(R.id.botView)

        textCount = findViewById(R.id.all_count)
        textLearnCount = findViewById(R.id.all_learn_count)

        ultraViewPager = findViewById(R.id.ultra_viewpager)
        ultraViewPager!!.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL)

        pageAdapterTest = PageAdapterTest(map!!, this, colorMap!!)

        ultraViewPager!!.adapter = pageAdapterTest
        ultraViewPager!!.setInfiniteLoop(true)
    }

    fun getCurrentItem(): Int {
        return ultraViewPager!!.currentItem
    }

    fun setPosition(position: Int) {
        ultraViewPager!!.currentItem = position
    }

    fun updateAdapter() {
        pageAdapterTest!!.notifyDataSetChanged()
    }

    fun getScaleX(): Float {
        val conf: ViewConfiguration = ViewConfiguration.get(this)
        val dm = resources.displayMetrics
        val result = conf.scaledPagingTouchSlop * dm.densityDpi / 160.0f + ultraViewPager!!.scaleX
        Log.d("SCROLL_X", "Scale: $result")
        return result
    }

    fun getScaleY(): Float {
        val conf: ViewConfiguration = ViewConfiguration.get(this)
        val dm = resources.displayMetrics
        val result = conf.scaledPagingTouchSlop * dm.densityDpi / 160.0f + ultraViewPager!!.scaleY
        Log.d("SCROLL_Y", "Scale: $result")
        return result
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    fun setTitleCount(position: Int, all: Int, learnCount: Int) {
        textCount!!.text = "$position in $all"
        textLearnCount!!.text = "Top count: $learnCount"
    }

    fun chooseColor(position: Int, colorMap: HashMap<Int, Int>) {
        when (colorMap[position]) {
            0 -> {
                topImage!!.setBackgroundColor(Color.BLACK)
                botImage!!.setBackgroundColor(Color.BLACK)
            }
            1 -> {
                topImage!!.setBackgroundColor(Color.RED)
                botImage!!.setBackgroundColor(Color.BLACK)
            }
            2 -> {
                botImage!!.setBackgroundColor(Color.GREEN)
                topImage!!.setBackgroundColor(Color.BLACK)
            }
        }
    }
}
