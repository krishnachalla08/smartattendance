package com.example.smartattendance

import android.graphics.*

class RectOverlay(rect: Rect, graphicOverlay: GraphicOverlay) :
    GraphicOverlay.Graphic(graphicOverlay) {
    private val mRectColor = Color.GREEN
    private val mStrokeWidth = 4.0f
    private val mRectPaint: Paint
    private val graphicOverlay: GraphicOverlay
    private val rect: Rect
    override fun draw(canvas: Canvas?) {
        val rectF = RectF(rect)
        rectF.left = translateX(rectF.left)
        rectF.right = translateX(rectF.right)
        rectF.top = translateX(rectF.top)
        rectF.bottom = translateX(rectF.bottom)
        canvas!!.drawRect(rectF, mRectPaint)
    }

    init {
        mRectPaint = Paint()
        mRectPaint.color = mRectColor
        mRectPaint.style = Paint.Style.STROKE
        mRectPaint.strokeWidth = mStrokeWidth
        this.graphicOverlay = graphicOverlay
        this.rect = rect
        postInvalidate()
    }
}