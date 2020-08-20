package com.mhamza007.iptv.dashboard

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.mhamza007.iptv.MainActivity
import com.mhamza007.iptv.MoviesActivity
import com.mhamza007.iptv.R
import kotlinx.android.synthetic.main.activity_dashboard_ser.*

class DashboardMovActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_dashboard_mov)

        logout.setOnClickListener {
            showLogoutDialog()
        }

        arrowUp.setOnClickListener {
            startActivity(Intent(this, DashboardSetActivity::class.java))
            overridePendingTransition(0, 0)
        }

        arrowDown.setOnClickListener {
            startActivity(Intent(this, DashboardLTVActivity::class.java))
            overridePendingTransition(0, 0)
        }

        text.setOnClickListener {
            startActivity(Intent(this, MoviesActivity::class.java))
            overridePendingTransition(0, 0)
        }

        icon.setOnClickListener {
            startActivity(Intent(this, MoviesActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    private fun showLogoutDialog() {
        dialog = Dialog(this@DashboardMovActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.setContentView(R.layout.exit_dialog)
        dialog.setCanceledOnTouchOutside(true)

        val exitYesBtn = dialog.findViewById<Button>(R.id.exitYes)
        val exitNoBtn = dialog.findViewById<Button>(R.id.exitNo)
        val exitText = dialog.findViewById<TextView>(R.id.exit_text)

        exitText.text = getString(R.string.logout_text)

        exitYesBtn.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@DashboardMovActivity, MainActivity::class.java))
            overridePendingTransition(
                R.anim.side_in_bottom,
                R.anim.slide_out_bottom
            )
            finish()
        }

        exitNoBtn.setOnClickListener {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showExitDialog() {
        dialog = Dialog(this@DashboardMovActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.setContentView(R.layout.exit_dialog)
        dialog.setCanceledOnTouchOutside(true)

        val exitYesBtn = dialog.findViewById<Button>(R.id.exitYes)
        val exitNoBtn = dialog.findViewById<Button>(R.id.exitNo)
        val exitText = dialog.findViewById<TextView>(R.id.exit_text)

        exitText.text = getString(R.string.exit_text)

        exitYesBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        exitNoBtn.setOnClickListener {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}