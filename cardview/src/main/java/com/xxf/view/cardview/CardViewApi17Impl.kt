package com.xxf.view.cardview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF


/**
 * create by bigman
 * create date == 2018/7/10
 * create time == 15:48
 */
internal class CardViewApi17Impl : CardViewBaseImpl() {


    override fun initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper = object : RoundRectDrawableWithShadow.RoundRectHelper {
            override fun drawRoundRect(canvas: Canvas, bounds: RectF, cornerRadius: Float, paint: Paint?) {
                if (paint != null) {
                    canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint)
                } else {
                    canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, Paint())
                }
            }
        }

    }

}