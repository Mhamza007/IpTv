package com.mhamza007.iptv.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mhamza007.iptv.R
import com.mhamza007.iptv.api.ActivationCodeAuthInterceptor
import com.mhamza007.iptv.api.ApiService
import com.mhamza007.iptv.api.AuthInterceptor
import com.mhamza007.iptv.api.Config
import com.mhamza007.iptv.dashboard.DashboardLTVActivity
import com.mhamza007.iptv.model.user.UserInfo
import com.mhamza007.iptv.util.SharedPreference
import com.mhamza007.iptv.util.Utils
import kotlinx.android.synthetic.main.activity_login_act_code.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginActCodeActivity : AppCompatActivity() {

    private lateinit var sharedPreference: SharedPreference
    private val timeout = 10L

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.checkThemeInfo(this)
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login_act_code)

        sharedPreference = SharedPreference(this@LoginActCodeActivity)

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
            val actCode = activationCode.text.toString().trim()
            if (actCode.isEmpty()) {
                activationCode.error = "Empty Code"
            } else {
                val client = OkHttpClient.Builder().addInterceptor(
                    AuthInterceptor(
                        getString(R.string.test_password),
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

                val call = request.loginWithUsername(actCode, actCode)

                call.enqueue(object : Callback<UserInfo> {
                    override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                        if (response.isSuccessful) {
                            val user = response.body()

                            try {
                                if (user != null) {
                                    sharedPreference.setActivationCode(actCode)

                                    val pair = Pair<View, String>(
                                        findViewById(R.id.logo),
                                        "imageTransition"
                                    )
                                    val intent = Intent(
                                        this@LoginActCodeActivity,
                                        DashboardLTVActivity::class.java
                                    )
                                    val options = ActivityOptions.makeSceneTransitionAnimation(
                                        this@LoginActCodeActivity,
                                        pair
                                    )
                                    startActivity(intent, options.toBundle())
                                    finishAfterTransition()
                                } else {
                                    Utils.toast(this@LoginActCodeActivity, "Login Failed")
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "onResponse Exception ${e.message}")
                                Utils.toast(this@LoginActCodeActivity, "Login Exception Occurred")
                            }
                        } else {
                            Utils.toast(this@LoginActCodeActivity, "Login Failed")
                        }
                    }

                    override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                        Log.e(TAG, "onFailure ${t.message}")
                        Log.e(TAG, "onFailure ${t.localizedMessage}")
                        Log.e(TAG, "onFailure ${t.cause}")
                        Utils.toast(this@LoginActCodeActivity, "Failure")
                    }
                })
            }
        }
    }

    companion object {
        const val TAG = "LoginActCodeActivity"
    }
}