package com.ext.android_datetimepicker


import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.ext.android_datetimepicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.parseColor("#000000") // black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0, // no light icons
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
