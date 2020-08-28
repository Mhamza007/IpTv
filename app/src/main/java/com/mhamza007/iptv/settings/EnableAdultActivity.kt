package com.mhamza007.iptv.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.mhamza007.iptv.R
import com.mhamza007.iptv.util.Utils
import kotlinx.android.synthetic.main.activity_enable_adult.*

class EnableAdultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_enable_adult)

        back.setOnClickListener {
            finish()
            overridePendingTransition(0,0)
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0,0)
    }
}