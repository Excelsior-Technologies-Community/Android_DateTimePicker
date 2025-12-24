package com.ext.android_datetimepicker

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.Calendar

class DateTimePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val calendar = Calendar.getInstance()

    private val dateTextView = TextView(context).apply {
        text = "Select Date"
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setPadding(32, 48, 32, 48)
    }

    private val timeTextView = TextView(context).apply {
        text = "Select Time"
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        setPadding(32, 48, 32, 48)
    }

    private var textColor: Int = Color.parseColor("#333333")
    private var textSize: Float = 18f
    private var fontFamily: String? = null
    private var showDate: Boolean = true
    private var showTime: Boolean = true
    private var pickerColor: Int = Color.parseColor("#6F4685")

    init {
        orientation = VERTICAL

        context.obtainStyledAttributes(attrs, R.styleable.DateTimePickerView).apply {
            textColor = getColor(R.styleable.DateTimePickerView_dtTextColor, textColor)
            textSize = getDimension(R.styleable.DateTimePickerView_dtTextSize, textSize)
            fontFamily = getString(R.styleable.DateTimePickerView_dtFontFamily)
            showDate = getBoolean(R.styleable.DateTimePickerView_dtShowDate, true)
            showTime = getBoolean(R.styleable.DateTimePickerView_dtShowTime, true)
            pickerColor = getColor(R.styleable.DateTimePickerView_dtPickerColor, pickerColor)
            recycle()
        }

        val typeface = fontFamily?.let { Typeface.create(it, Typeface.NORMAL) } ?: Typeface.DEFAULT

        dateTextView.apply {
            setTextColor(textColor)
            textSize = this@DateTimePickerView.textSize / resources.displayMetrics.scaledDensity
            this.typeface = typeface
        }

        timeTextView.apply {
            setTextColor(textColor)
            textSize = this@DateTimePickerView.textSize / resources.displayMetrics.scaledDensity
            this.typeface = typeface
        }

        dateTextView.setOnClickListener { openDatePicker() }
        timeTextView.setOnClickListener { openTimePicker() }

        if (showDate) addView(dateTextView)
        if (showTime) addView(timeTextView)
        if (!showDate && !showTime) minimumHeight = 100
    }

    private fun openDatePicker() {
        val dialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                dateTextView.text = String.format("%02d/%02d/%d", day, month + 1, year)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.setOnShowListener {
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)?.setTextColor(pickerColor)
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)?.setTextColor(pickerColor)
            changeDatePickerColors(dialog.datePicker)
        }

        dialog.show()
    }

    private fun openTimePicker() {
        val dialog = TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                timeTextView.text = String.format("%02d:%02d", hour, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )

        dialog.setOnShowListener {
            val timePickerId = context.resources.getIdentifier("timePicker", "id", "android")
            val timePicker = dialog.findViewById<TimePicker>(timePickerId)

            timePicker?.let {
                // Change all time picker colors
                changeAllTimePickerColors(it)
            }

            // Change Cancel and OK buttons to RED (AFTER changing colors to avoid button shape issue)
            dialog.getButton(TimePickerDialog.BUTTON_POSITIVE)?.apply {
                setTextColor(pickerColor)
                background = null // Remove background to prevent red oval shape
            }
            dialog.getButton(TimePickerDialog.BUTTON_NEGATIVE)?.apply {
                setTextColor(pickerColor)
                background = null // Remove background to prevent red oval shape
            }
        }

        dialog.show()
    }

    private fun changeDatePickerColors(datePicker: DatePicker) {
        try {
            // Change header background to RED
            val headerId = context.resources.getIdentifier("date_picker_header", "id", "android")
            val headerView = datePicker.findViewById<View>(headerId)
            headerView?.setBackgroundColor(pickerColor)

            // Change header text to WHITE
            val headerYearId = context.resources.getIdentifier("date_picker_header_year", "id", "android")
            val headerDateId = context.resources.getIdentifier("date_picker_header_date", "id", "android")

            datePicker.findViewById<TextView>(headerYearId)?.setTextColor(Color.WHITE)
            datePicker.findViewById<TextView>(headerDateId)?.setTextColor(Color.WHITE)

            // Change selected date circle to RED
            tintAllViews(datePicker)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeAllTimePickerColors(timePicker: TimePicker) {
        try {
            // ========== STEP 1: CHANGE HEADER BACKGROUND ==========
            // This works for BOTH text input mode (Image 3) AND clock mode (Images 1 & 2)
            val headerIds = listOf(
                "time_header",           // Clock mode header
                "timePickerHeader",      // Alternative name
                "header"                 // Generic header
            )

            for (headerId in headerIds) {
                try {
                    val id = context.resources.getIdentifier(headerId, "id", "android")
                    if (id != 0) {
                        val headerView = timePicker.findViewById<View>(id)
                        headerView?.setBackgroundColor(pickerColor)
                    }
                } catch (e: Exception) {
                    // Try next ID
                }
            }

            // ========== STEP 2: CHANGE HEADER TEXT TO WHITE ==========
            val textIds = mapOf(
                "hours" to Color.WHITE,
                "minutes" to Color.WHITE,
                "separator" to Color.WHITE,
                "am_label" to Color.WHITE,
                "pm_label" to Color.WHITE,
                "hour" to Color.WHITE,
                "minute" to Color.WHITE
            )

            for ((idName, color) in textIds) {
                try {
                    val id = context.resources.getIdentifier(idName, "id", "android")
                    if (id != 0) {
                        timePicker.findViewById<TextView>(id)?.setTextColor(color)
                    }
                } catch (e: Exception) {
                    // Continue
                }
            }

            // ========== STEP 3: CHANGE CLOCK COLORS (Images 1 & 2) ==========
            changeRadialClockColors(timePicker)

            // ========== STEP 4: CHANGE NUMBER PICKER COLORS (Image 3 - Text Input) ==========
            changeAllNumberPickers(timePicker)

            // ========== STEP 5: TINT ALL OTHER VIEWS ==========
            tintAllViews(timePicker)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeRadialClockColors(timePicker: TimePicker) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val delegateField = TimePicker::class.java.getDeclaredField("mDelegate")
                delegateField.isAccessible = true
                val delegate = delegateField.get(timePicker) ?: return

                val radialFieldNames = listOf("mRadialTimePickerView", "mRadialPickerView", "radialTimePickerView")
                var radialView: Any? = null

                for (fieldName in radialFieldNames) {
                    try {
                        val field = delegate.javaClass.getDeclaredField(fieldName)
                        field.isAccessible = true
                        radialView = field.get(delegate)
                        if (radialView != null) break
                    } catch (e: Exception) {
                        // Try next field name
                    }
                }

                if (radialView == null) return

                // Change CLOCK HAND (selector line)
                changePaintField(radialView, listOf("mPaintSelector", "mSelectorPaint", "mPaintSelectorCenter"))

                // Change CENTER DOT
                changePaintField(radialView, listOf("mPaintCenter", "mCenterPaint", "mPaintCenterDot"))

                // Change PURPLE CIRCLE BACKGROUND (selected number)
                changePaintField(radialView, listOf("mPaintBackground", "mSelectionPaint", "mPaintBackgroundSelected"))

                // Change color integer fields
                changeColorIntField(radialView, listOf("mSelectorColor", "mSelectionColor", "mColorBackground"))

                // Invalidate to redraw
                (radialView as? View)?.invalidate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changePaintField(obj: Any, fieldNames: List<String>) {
        for (fieldName in fieldNames) {
            try {
                val field = obj.javaClass.getDeclaredField(fieldName)
                field.isAccessible = true
                val paint = field.get(obj)
                if (paint is android.graphics.Paint) {
                    paint.color = pickerColor
                    paint.alpha = 255
                }
            } catch (e: Exception) {
                // Try next field
            }
        }
    }

    private fun changeColorIntField(obj: Any, fieldNames: List<String>) {
        for (fieldName in fieldNames) {
            try {
                val field = obj.javaClass.getDeclaredField(fieldName)
                field.isAccessible = true
                field.set(obj, pickerColor)
            } catch (e: Exception) {
                // Try next field
            }
        }
    }

    private fun changeAllNumberPickers(view: View) {
        try {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val child = view.getChildAt(i)

                    if (child is NumberPicker) {
                        applyNumberPickerColor(child)
                    }

                    changeAllNumberPickers(child)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyNumberPickerColor(numberPicker: NumberPicker) {
        try {
            // Change divider color to RED
            val dividerField = NumberPicker::class.java.getDeclaredField("mSelectionDivider")
            dividerField.isAccessible = true
            dividerField.set(numberPicker, ColorDrawable(pickerColor))

            // Change text color to RED
            for (i in 0 until numberPicker.childCount) {
                val child = numberPicker.getChildAt(i)
                if (child is EditText) {
                    child.setTextColor(pickerColor)
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            child.textCursorDrawable?.setTint(pickerColor)
                        }
                    } catch (e: Exception) {
                        // Cursor tinting may fail
                    }
                }
            }

            numberPicker.invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun tintAllViews(view: View) {
        try {
            view.background?.let { drawable ->
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintList(ColorStateList.valueOf(pickerColor))
                    } else {
                        drawable.setColorFilter(pickerColor, PorterDuff.Mode.SRC_ATOP)
                    }
                } catch (e: Exception) {
                    // Some drawables don't support tinting
                }
            }

            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    tintAllViews(view.getChildAt(i))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setPickerColor(color: Int) {
        pickerColor = color
    }

    fun getSelectedDate(): String = dateTextView.text.toString()
    fun getSelectedTime(): String = timeTextView.text.toString()
    fun getSelectedDateTime(): String = "${getSelectedDate()} ${getSelectedTime()}"
}




