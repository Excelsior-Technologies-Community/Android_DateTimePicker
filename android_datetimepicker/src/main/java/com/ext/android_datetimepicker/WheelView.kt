package com.ext.android_datetimepicker

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import android.widget.NumberPicker
import java.lang.reflect.Field

class WheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : NumberPicker(context, attrs) {

    var valueString: String = ""
    private var selectedTextColor: Int = Color.RED
    private var defaultTextColor: Int = Color.DKGRAY

    fun setup(min: Int, max: Int, textColor: Int, textSize: Float, fontFamily: String?) {
        minValue = min
        maxValue = max
        wrapSelectorWheel = true
        selectedTextColor = textColor
        defaultTextColor = Color.DKGRAY

        post {
            applyTextSize(textSize)
            applyFont(fontFamily)
            updateTextColors()
        }

        setOnValueChangedListener { _, _, _ -> updateTextColors() }
    }

    fun setup(values: List<String>, textColor: Int, textSize: Float, fontFamily: String?) {
        displayedValues = values.toTypedArray()
        minValue = 0
        maxValue = values.size - 1
        wrapSelectorWheel = false
        selectedTextColor = textColor
        defaultTextColor = Color.DKGRAY

        post {
            applyTextSize(textSize)
            applyFont(fontFamily)
            updateTextColors()
        }

        setOnValueChangedListener { _, _, _ -> updateTextColors() }
    }

    override fun setValue(value: Int) {
        super.setValue(value)
        valueString = displayedValues?.getOrNull(value) ?: value.toString()
        post { updateTextColors() }
    }

    private fun updateTextColors() {
        try {
            val selectorWheelPaintField: Field =
                NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            val paint = selectorWheelPaintField.get(this) as android.graphics.Paint
            paint.color = selectedTextColor

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is EditText) {
                    child.setTextColor(if (child.isFocused) selectedTextColor else defaultTextColor)
                }
            }

            invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyTextSize(sizePx: Float) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) {
                child.textSize = sizePx / resources.displayMetrics.scaledDensity
            }
        }
    }

    private fun applyFont(fontFamily: String?) {
        if (fontFamily == null) return
        val typeface = Typeface.create(fontFamily, Typeface.NORMAL)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is EditText) child.typeface = typeface
        }
    }
}
