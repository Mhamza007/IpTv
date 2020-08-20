package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.mhamza007.iptv.settings.ChangeCodeActivity
import com.mhamza007.iptv.settings.EnableAdultActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_settings)

        back.setOnClickListener {
            finish()
        }

        liveTv.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, LiveTvActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@SettingsActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        movies.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, MoviesActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@SettingsActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        series.setOnClickListener {
            val pair =
                Pair<View, String>(
                    findViewById(R.id.selection),
                    getString(R.string.selectionTransition)
                )
            val intent = Intent(this, SeriesActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@SettingsActivity,
                pair
            )
            startActivity(intent, options.toBundle())
            finishAfterTransition()
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(this, "Searching ", Toast.LENGTH_SHORT).show()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        enableAdultCategory.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, EnableAdultActivity::class.java))
            overridePendingTransition(0, 0)
        }

        changeCode.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, ChangeCodeActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }
}