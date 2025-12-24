package com.ext.android_datetimepicker

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.Calendar

class WheelTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val calendar = Calendar.getInstance()

    private val timeTextView = TextView(context).apply {
        text = "Select Time"
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setPadding(48, 64, 48, 64)
    }

    private var textColor: Int = Color.parseColor("#333333")
    private var textSize: Float = 18f
    private var fontFamily: String? = null
    private var wheelTextColor: Int = Color.RED
    private var buttonTextColor: Int = Color.RED

    init {
        orientation = VERTICAL

        context.obtainStyledAttributes(attrs, R.styleable.DateTimePickerView).apply {
            textColor = getColor(R.styleable.DateTimePickerView_dtTextColor, textColor)
            textSize = getDimension(R.styleable.DateTimePickerView_dtTextSize, textSize)
            fontFamily = getString(R.styleable.DateTimePickerView_dtFontFamily)
            wheelTextColor = getColor(R.styleable.DateTimePickerView_dtWheelTextColor, wheelTextColor)
            buttonTextColor = getColor(R.styleable.DateTimePickerView_dtDialogButtonTextColor, buttonTextColor)
            recycle()
        }

        timeTextView.apply {
            setTextColor(textColor)
            textSize = this@WheelTimePickerView.textSize / resources.displayMetrics.scaledDensity
            typeface = fontFamily?.let { Typeface.create(it, Typeface.NORMAL) } ?: Typeface.DEFAULT
        }

        timeTextView.setOnClickListener { showTimeWheelDialog() }
        addView(timeTextView)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showTimeWheelDialog() {

        val dialogLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(24, 24, 24, 24)
        }

        val wheelParams = LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            1f
        )

        val hourWheel = WheelView(context).apply {
            layoutParams = wheelParams
            setup(1, 12, wheelTextColor, textSize, fontFamily)
        }

        val minuteWheel = WheelView(context).apply {
            layoutParams = wheelParams
            setup(0, 59, wheelTextColor, textSize, fontFamily)
        }

        val amPmWheel = WheelView(context).apply {
            layoutParams = wheelParams
            setup(listOf("AM", "PM"), wheelTextColor, textSize, fontFamily)
        }

        // Set current time
        var displayHour = calendar.get(Calendar.HOUR)
        if (displayHour == 0) displayHour = 12

        hourWheel.value = displayHour
        minuteWheel.value = calendar.get(Calendar.MINUTE)
        amPmWheel.value = if (calendar.get(Calendar.AM_PM) == Calendar.PM) 1 else 0

        dialogLayout.addView(hourWheel)
        dialogLayout.addView(minuteWheel)
        dialogLayout.addView(amPmWheel)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogLayout)
            .setPositiveButton("OK") { _, _ ->
                val hour = hourWheel.value
                val minute = minuteWheel.value
                val amPm = amPmWheel.valueString

                timeTextView.text = String.format("%02d:%02d %s", hour, minute, amPm)

                val hour24 = when {
                    amPm == "PM" && hour != 12 -> hour + 12
                    amPm == "AM" && hour == 12 -> 0
                    else -> hour
                }
                calendar.set(Calendar.HOUR_OF_DAY, hour24)
                calendar.set(Calendar.MINUTE, minute)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        // Set button text colors from XML
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(buttonTextColor)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(buttonTextColor)
    }

    fun getSelectedTime(): String = timeTextView.text.toString()
}
