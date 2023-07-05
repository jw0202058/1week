package com.example.a1week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircularImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val clipPath = Path()

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE // 원의 색상 설정
    }

    override fun onDraw(canvas: Canvas) {
        val radius = width / 2f
        canvas.drawCircle(radius, radius, radius, paint) // 원형으로 그리기
        clipPath.addCircle(radius, radius, radius, Path.Direction.CW) // 클리핑 경로 설정
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }
}
