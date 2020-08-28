package com.mhamza007.iptv.dashboard

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mhamza007.iptv.LiveTvActivity
import com.mhamza007.iptv.MainActivity
import com.mhamza007.iptv.R
import com.mhamza007.iptv.util.Utils
import kotlinx.android.synthetic.main.activity_dashboard_ltv.*

class DashboardLTVActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_dashboard_ltv)

        logout.setOnClickListener {
            showLogoutDialog()
        }

        arrowUp.setOnClickListener {
            startActivity(Intent(this, DashboardMovActivity::class.java))
            overridePendingTransition(0,0)
        }

        arrowDown.setOnClickListener {
            startActivity(Intent(this, DashboardSerActivity::class.java))
            overridePendingTransition(0,0)
        }

        text.setOnClickListener {
            startActivity(Intent(this, LiveTvActivity::class.java))
            overridePendingTransition(0,0)
        }

        icon.setOnClickListener {
            startActivity(Intent(this, LiveTvActivity::class.java))
            overridePendingTransition(0,0)
        }
    }

    companion object {
        const val TAG = "DashboardActivity"
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    private fun showLogoutDialog() {
        dialog = Dialog(this@DashboardLTVActivity)
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
            startActivity(Intent(this@DashboardLTVActivity, MainActivity::class.java))
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
        dialog = Dialog(this@DashboardLTVActivity)
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

//class MyItem(private val context: Context, private val string : String): Item<GroupieViewHolder>() {
//    override fun getLayout(): Int {
//        return R.layout.sub_category_item
//    }
//
//    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//        viewHolder.itemView.subCategoryTitle.text = string
//    }
//
//}