package com.mhamza007.iptv

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mhamza007.iptv.api.ApiService
import com.mhamza007.iptv.api.Config
import com.mhamza007.iptv.model.user.UserInfo
import com.mhamza007.iptv.settings.ChangeCodeActivity
import com.mhamza007.iptv.settings.EnableAdultActivity
import com.mhamza007.iptv.util.SharedPreference
import com.mhamza007.iptv.util.Utils
import kotlinx.android.synthetic.main.activity_settings.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
    private val timeout = 10L

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_settings)

        sharedPreference = SharedPreference(this@SettingsActivity)

        theme1.setOnClickListener {
            sharedPreference.setTheme(1)
            restartActivity()
        }

        theme2.setOnClickListener {
            sharedPreference.setTheme(2)
            restartActivity()
        }

        theme3.setOnClickListener {
            sharedPreference.setTheme(3)
            restartActivity()
        }

        theme4.setOnClickListener {
            sharedPreference.setTheme(4)
            restartActivity()
        }

        theme5.setOnClickListener {
            sharedPreference.setTheme(5)
            restartActivity()
        }

        theme6.setOnClickListener {
            sharedPreference.setTheme(6)
            restartActivity()
        }

        if (sharedPreference.getActivationCode() != "")
            activationCode.text = sharedPreference.getActivationCode()
        else
            actCodeRow.visibility = View.GONE

        version.text = "1.0"

        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request = retrofit.create(ApiService::class.java)

        val call = if (sharedPreference.getActivationCode() != "") {
            request.getUserInfo(
                sharedPreference.getUserName(),
                sharedPreference.getPassword()
            )
        } else {
            request.getUserInfo(
                sharedPreference.getActivationCode(),
                sharedPreference.getActivationCode()
            )
        }

        call.enqueue(object : Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                if (response.isSuccessful) {
                    val userInfo = response.body()

                    try {
                        if (userInfo != null) {
                            val formatter = SimpleDateFormat("dd-MM-yyyy")
                            val calender = Calendar.getInstance()
                            calender.timeInMillis = userInfo.exp_date.toLong()
                            expiresOn.text = formatter.format(calender.time)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse Exception $e")
                    }
                } else {
                    Log.e(TAG, "Response Failed")
                }
            }

            override fun onFailure(call: Call<UserInfo>, t: Throwable) {

            }
        })

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

    private fun restartActivity() {
        startActivity(Intent(this@SettingsActivity, SettingsActivity::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    companion object {
        const val TAG = "SettingsActivity"
    }
}