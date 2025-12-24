package com.ext.android_datetimepicker

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Calendar

class WheelDateTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val calendar = Calendar.getInstance()

    private val dateTextView = TextView(context).apply {
        text = "Select Date"
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setPadding(48, 64, 48, 64)
    }

    private var textColor = Color.parseColor("#333333")
    private var wheelTextColor = Color.BLACK
    private var dialogButtonTextColor = Color.BLACK
    private var textSize = 18f
    private var fontFamily: String? = null

    init {
        orientation = VERTICAL

        context.obtainStyledAttributes(attrs, R.styleable.DateTimePickerView).apply {
            textColor = getColor(R.styleable.DateTimePickerView_dtTextColor, textColor)
            wheelTextColor =
                getColor(R.styleable.DateTimePickerView_dtWheelTextColor, textColor)
            dialogButtonTextColor =
                getColor(R.styleable.DateTimePickerView_dtDialogButtonTextColor, Color.BLACK)
            textSize = getDimension(R.styleable.DateTimePickerView_dtTextSize, textSize)
            fontFamily = getString(R.styleable.DateTimePickerView_dtFontFamily)
            recycle()
        }

        val typeface =
            fontFamily?.let { Typeface.create(it, Typeface.NORMAL) } ?: Typeface.DEFAULT

        dateTextView.apply {
            setTextColor(textColor)
            textSize = this@WheelDateTimePickerView.textSize /
                    resources.displayMetrics.scaledDensity
            this.typeface = typeface
        }

        dateTextView.setOnClickListener { showDateWheelDialog() }
        addView(dateTextView)
    }

    private fun showDateWheelDialog() {

        val dialogLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(24, 24, 24, 24)
        }

        val wheelParams = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

        val dayWheel = WheelView(context).apply {
            layoutParams = wheelParams
            setup(1, 31, wheelTextColor, textSize, fontFamily)
            value = calendar.get(Calendar.DAY_OF_MONTH)
        }

        val monthWheel = WheelView(context).apply {
            layoutParams = wheelParams
            setup(1, 12, wheelTextColor, textSize, fontFamily)
            value = calendar.get(Calendar.MONTH) + 1
        }

        val yearWheel = WheelView(context).apply {
            layoutParams = wheelParams
            setup(1990, 2035, wheelTextColor, textSize, fontFamily)
            value = calendar.get(Calendar.YEAR)
        }

        dialogLayout.addView(dayWheel)
        dialogLayout.addView(monthWheel)
        dialogLayout.addView(yearWheel)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogLayout)
            .setPositiveButton("OK") { _, _ ->
                val day = dayWheel.value
                val month = monthWheel.value - 1
                val year = yearWheel.value
                dateTextView.text =
                    String.format("%02d/%02d/%d", day, month + 1, year)
                calendar.set(year, month, day)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        // 🔴 Apply OK / Cancel color
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(dialogButtonTextColor)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(dialogButtonTextColor)
    }

    fun getSelectedDate(): String = dateTextView.text.toString()
}
