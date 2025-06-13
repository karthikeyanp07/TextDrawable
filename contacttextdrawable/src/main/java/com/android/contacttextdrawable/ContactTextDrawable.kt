package com.android.contacttextdrawable

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import java.util.Locale

class ContactTextDrawable private constructor(private val builder: Builder) : ShapeDrawable(builder.shape)
{
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = builder.textColor
        isFakeBoldText = builder.isBold
        style = Paint.Style.FILL
        typeface = builder.font
        textAlign = Paint.Align.CENTER
        strokeWidth = builder.borderThickness.toFloat()
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = getDarkerShade(builder.color)
        style = Paint.Style.STROKE
        strokeWidth = builder.borderThickness.toFloat()
    }

    private val text: String = if (builder.toUpperCase) {
        builder.text.toUpperCase(Locale.ROOT)
    }
    else
    {
        builder.text
    }

    init
    {
        paint.color = builder.color
    }

    private fun getDarkerShade(color: Int): Int {
        val factor = SHADE_FACTOR
        return Color.rgb(
            (Color.red(color) * factor).toInt(),
            (Color.green(color) * factor).toInt(),
            (Color.blue(color) * factor).toInt()
        )
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val bounds = bounds

        if (builder.borderThickness > 0) {
            drawBorder(canvas, bounds)
        }

        val count = canvas.save()
        canvas.translate(bounds.left.toFloat(), bounds.top.toFloat())

        val width = if (builder.width < 0) bounds.width() else builder.width
        val height = if (builder.height < 0) bounds.height() else builder.height
        val fontSize = if (builder.fontSize < 0) minOf(width, height) / 2 else builder.fontSize

        textPaint.textSize = fontSize.toFloat()

        val xPos = width / 2f
        val yPos = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(text, xPos, yPos, textPaint)

        canvas.restoreToCount(count)
    }

    private fun drawBorder(canvas: Canvas, bounds: Rect) {
        val rect = RectF(bounds)
        rect.inset(builder.borderThickness / 2f, builder.borderThickness / 2f)

        when (builder.shape) {
            is OvalShape -> canvas.drawOval(rect, borderPaint)
            is RoundRectShape -> canvas.drawRoundRect(rect, builder.radius, builder.radius, borderPaint)
            else -> canvas.drawRect(rect, borderPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        textPaint.colorFilter = cf
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int = builder.width
    override fun getIntrinsicHeight(): Int = builder.height

    companion object {
        private const val SHADE_FACTOR = 0.9f
        fun builder(): IShapeBuilder = Builder()
    }

    // Interfaces for Fluent Builder Pattern
    interface IConfigBuilder {
        fun width(width: Int): IConfigBuilder
        fun height(height: Int): IConfigBuilder
        fun textColor(color: Int): IConfigBuilder
        fun withBorder(thickness: Int): IConfigBuilder
        fun useFont(font: Typeface): IConfigBuilder
        fun fontSize(size: Int): IConfigBuilder
        fun bold(): IConfigBuilder
        fun toUpperCase(): IConfigBuilder
        fun endConfig(): IShapeBuilder
    }

    interface IBuilder {
        fun build(text: String, color: Int): ContactTextDrawable
    }

    interface IShapeBuilder {
        fun beginConfig(): IConfigBuilder
        fun rect(): IBuilder
        fun round(): IBuilder
        fun roundRect(radius: Int): IBuilder
        fun buildRect(text: String, color: Int): ContactTextDrawable
        fun buildRoundRect(text: String, color: Int, radius: Int): ContactTextDrawable
        fun buildRound(text: String, color: Int): ContactTextDrawable
    }

    // Builder class
    class Builder : IConfigBuilder, IShapeBuilder, IBuilder {
        var text: String = ""
        var color: Int = Color.GRAY
        var textColor: Int = Color.WHITE
        var borderThickness: Int = 0
        var width: Int = -1
        var height: Int = -1
        var font: Typeface = Typeface.DEFAULT_BOLD
        var shape: RectShape = RectShape()
        var fontSize: Int = -1
        var isBold: Boolean = false
        var toUpperCase: Boolean = false
        var radius: Float = 0f

        override fun width(width: Int) = apply { this.width = width }
        override fun height(height: Int) = apply { this.height = height }
        override fun textColor(color: Int) = apply { this.textColor = color }
        override fun withBorder(thickness: Int) = apply { this.borderThickness = thickness }
        override fun useFont(font: Typeface) = apply { this.font = font }
        override fun fontSize(size: Int) = apply { this.fontSize = size }
        override fun bold() = apply { this.isBold = true }
        override fun toUpperCase() = apply { this.toUpperCase = true }

        override fun beginConfig(): IConfigBuilder = this
        override fun endConfig(): IShapeBuilder = this

        override fun rect(): IBuilder {
            shape = RectShape()
            return this
        }

        override fun round(): IBuilder {
            shape = OvalShape()
            return this
        }

        override fun roundRect(radius: Int): IBuilder {
            this.radius = radius.toFloat()
            val radii = FloatArray(8) { this.radius }
            shape = RoundRectShape(radii, null, null)
            return this
        }

        override fun buildRect(text: String, color: Int): ContactTextDrawable {
            return rect().build(text, color)
        }

        override fun buildRoundRect(text: String, color: Int, radius: Int): ContactTextDrawable {
            return roundRect(radius).build(text, color)
        }

        override fun buildRound(text: String, color: Int): ContactTextDrawable {
            return round().build(text, color)
        }

        override fun build(text: String, color: Int): ContactTextDrawable {
            this.color = color
            this.text = processText(text)
            return ContactTextDrawable(this)
        }

        private fun processText(text: String): String {
            val words = text.trim().split("\\s+".toRegex())
            return when {
                words.size >= 2 -> "${words[0].first()}${words[1].first()}"
                words.isNotEmpty() -> words[0].take(2)
                else -> ""
            }
        }
    }
}