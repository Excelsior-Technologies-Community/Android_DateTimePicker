package com.ext.android_datetimepicker

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.ext.android_datetimepicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate binding first
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set status bar color
        window.statusBarColor = Color.parseColor("#000000") // black

        // Handle light/dark status bar icons safely
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use the WindowInsetsController safely
            window.decorView?.let { decorView ->
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    0, // clear APPEARANCE_LIGHT_STATUS_BARS to use light icons
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        } else {
            // For older devices, use legacy flags
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}
