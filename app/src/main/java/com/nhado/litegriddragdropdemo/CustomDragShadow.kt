package com.nhado.litegriddragdropdemo

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class CustomDragShadow(view: View?) : View.DragShadowBuilder(view) {

    override fun onProvideShadowMetrics(shadowSize: Point, shadowTouchPoint: Point) {
        shadowSize.set(view.width, view.height);
        shadowTouchPoint.set(view.width / 2, view.height/2);
    }

    override fun onDrawShadow(canvas: Canvas) {

        view.draw(canvas)
    }
}