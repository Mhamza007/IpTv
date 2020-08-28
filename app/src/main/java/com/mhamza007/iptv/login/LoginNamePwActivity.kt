package com.mhamza007.iptv.login

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import com.mhamza007.iptv.R
import com.mhamza007.iptv.api.ApiService
import com.mhamza007.iptv.api.AuthInterceptor
import com.mhamza007.iptv.api.Config
import com.mhamza007.iptv.dashboard.DashboardLTVActivity
import com.mhamza007.iptv.model.user.UserInfo
import com.mhamza007.iptv.util.SharedPreference
import com.mhamza007.iptv.util.Utils
import kotlinx.android.synthetic.main.activity_login_name_pw.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginNamePwActivity : AppCompatActivity() {
    private lateinit var sharedPreference: SharedPreference
    private val timeout = 10L

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login_name_pw)

        sharedPreference = SharedPreference(this@LoginNamePwActivity)

        back.setOnClickListener {
            onBackPressed()
        }

        if (sharedPreference.getRememberCredentials() && sharedPreference.getUserName() != "" && sharedPreference.getPassword() != "") {
            rememberMe.isChecked = sharedPreference.getRememberCredentials()
            username.setText(sharedPreference.getUserName())
            password.setText(sharedPreference.getPassword())
        }

        login.setOnClickListener {
            if (username.text.toString().trim().isEmpty() && password.text.toString().trim()
                    .isEmpty()
            ) {
                username.error = "Empty username"
                password.error = "Empty password"
            } else if (username.text.toString().trim().isEmpty()) {
                username.error = "Empty username"
            } else if (password.text.toString().trim().isEmpty()) {
                password.error = "Empty password"
            } else {
                sharedPreference.setUserName(username.text.toString().trim())
                sharedPreference.setPassword(password.text.toString().trim())
                if (rememberMe.isChecked) {
                    sharedPreference.setRememberCredentials(true)
                }

                val client = OkHttpClient.Builder().addInterceptor(
                    AuthInterceptor(
                        getString(R.string.test_username),
                        getString(R.string.test_password)
                    )
                ).connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val request = retrofit.create(ApiService::class.java)

                val call = request.loginWithUsername(
                    username.text.toString().trim(),
                    password.text.toString().trim()
                )

                call.enqueue(object : Callback<UserInfo> {
                    override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                        Log.e(TAG, "onFailure ${t.message}")
                        Utils.toast(this@LoginNamePwActivity, "Failure")
                    }

                    override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                        if (response.isSuccessful) {
                            val user = response.body()

                            try {
                                if (user != null) {

                                    val pair =
                                        Pair<View, String>(
                                            findViewById(R.id.logo),
                                            "imageTransition"
                                        )
                                    val intent = Intent(
                                        this@LoginNamePwActivity,
                                        DashboardLTVActivity::class.java
                                    )
                                    val options = ActivityOptions.makeSceneTransitionAnimation(
                                        this@LoginNamePwActivity,
                                        pair
                                    )
                                    startActivity(intent, options.toBundle())
                                    finishAfterTransition()
                                } else {
                                    Utils.toast(this@LoginNamePwActivity, "Login Failed")
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "onResponse Exception ${e.message}")
                                Utils.toast(this@LoginNamePwActivity, "Login Exception Occurred")
                            }
                        } else {
                            Utils.toast(this@LoginNamePwActivity, "Login Failed")
                        }
                    }
                })
            }
        }
    }

    companion object {
        const val TAG = "LoginNamePwActivity"
    }
}