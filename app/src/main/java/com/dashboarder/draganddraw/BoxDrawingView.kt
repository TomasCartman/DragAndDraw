package com.dashboarder.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingView"

class BoxDrawingView(context: Context, attrs: AttributeSet? = null)
    : View(context, attrs) {

    private var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(canvas != null) {
            // Fill the background
            canvas.drawPaint(backgroundPaint)
            boxen.forEach { box ->
                canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event != null) {
            val current = event.let { PointF(it.x, event.y) }
            var action = ""
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    action = "ACTION_DOWN"
                    // Reset drawing state
                    currentBox = Box(current).also {
                        boxen.add(it)
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    action = "ACTION_MOVE"
                    updateCurrentBox(current)
                }
                MotionEvent.ACTION_UP -> {
                    action = "ACTION_UP"
                    updateCurrentBox(current)
                    currentBox = null
                }
                MotionEvent.ACTION_CANCEL -> {
                    action = "ACTION_CANCEL"
                    currentBox = null
                }
            }
            Log.i(TAG, "$action at x=${current.x}, y=${current.y}")
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }
}