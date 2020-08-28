package com.mhamza007.iptv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mhamza007.iptv.util.Utils

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}