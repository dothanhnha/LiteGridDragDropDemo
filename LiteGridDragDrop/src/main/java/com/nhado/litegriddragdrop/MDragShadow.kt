package com.example.drapdropcustom

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class MDragShadow(view: View?) : View.DragShadowBuilder(view) {

    override fun onProvideShadowMetrics(shadowSize: Point, shadowTouchPoint: Point) {
        shadowSize.set(view.width, view.height);
        shadowTouchPoint.set(view.width / 2, view.height);
    }

    override fun onDrawShadow(canvas: Canvas) {
        view.draw(canvas)
    }
}