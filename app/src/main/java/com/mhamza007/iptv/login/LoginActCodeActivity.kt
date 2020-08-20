package com.mhamza007.iptv.login

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import com.mhamza007.iptv.R
import com.mhamza007.iptv.dashboard.DashboardLTVActivity
import kotlinx.android.synthetic.main.activity_login_act_code.*
import kotlinx.android.synthetic.main.activity_login_act_code.login

class LoginActCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login_act_code)

        click.setOnClickListener {
            val pair = arrayOfNulls<Pair<View, String>>(4)
            pair[0] = Pair(findViewById(R.id.login_txt), "loginTextAnimation")
            pair[1] = Pair(findViewById(R.id.activationCode), "loginFieldAnimation")
            pair[2] = Pair(findViewById(R.id.login), "loginBtnAnimation")
            pair[3] = Pair(findViewById(R.id.supportLayout), "supportLayoutAnimation")

            val intent = Intent(this, LoginNamePwActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@LoginActCodeActivity,
                *pair
            )
            startActivity(intent, options.toBundle())
        }

        login.setOnClickListener {
            val pair = Pair<View, String>(findViewById(R.id.logo), "imageTransition")
            val intent = Intent(this, DashboardLTVActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@LoginActCodeActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }
    }
}