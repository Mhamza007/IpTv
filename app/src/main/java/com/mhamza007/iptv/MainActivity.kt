package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import com.mhamza007.iptv.login.LoginActCodeActivity
import com.mhamza007.iptv.util.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        val pair = Pair<View, String>(findViewById(R.id.logo), "imageTransition")

        getStarted.setOnClickListener {
            val intent = Intent(this, LoginActCodeActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@MainActivity,
                pair
            )
            startActivity(intent, options.toBundle())
        }
    }

    override fun onBackPressed() {}
}