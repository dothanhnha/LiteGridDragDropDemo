package com.example.drapdropcustom

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class MDragShadow(view: View?) : View.DragShadowBuilder(view) {
/*    var width: Int = 0
    var height: Int = 0

    init {
        val rotationRad = Math.toRadians(30.0)
        val w = (view!!.width * view!!.scaleX).toInt()
        val h = (view!!.height * view!!.scaleY).toInt()
        val s = Math.abs(Math.sin(rotationRad))
        val c = Math.abs(Math.cos(rotationRad))
        width = (w * c + h * s).toInt()
        height = (w * s + h * c).toInt()
    }*/

    override fun onProvideShadowMetrics(shadowSize: Point, shadowTouchPoint: Point) {
        shadowSize.set(view.width, view.height);
        shadowTouchPoint.set(view.width / 2, view.height);
    }

    override fun onDrawShadow(canvas: Canvas) {
/*        canvas.rotate(30f, width / 2f, height / 2f);
        canvas.translate((width - view.getWidth()) / 2f,
                (height - view.getHeight()) / 2f)*/
        view.draw(canvas)
    }


}