package com.mhamza007.iptv.util

import android.content.Context
import android.widget.Toast
import com.mhamza007.iptv.R

class Utils {
    companion object {
        fun toast(context: Context, string: String) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
        }

        fun checkThemeInfo(context: Context) {
            val sharedPreference = SharedPreference(context)
            when {
                sharedPreference.getTheme() == 1 -> {
                    context.setTheme(R.style.ThemeOne)
                }
                sharedPreference.getTheme() == 2 -> {
                    context.setTheme(R.style.ThemeTwo)
                }
                sharedPreference.getTheme() == 3 -> {
                    context.setTheme(R.style.ThemeThree)
                }
                sharedPreference.getTheme() == 4 -> {
                    context.setTheme(R.style.ThemeFour)
                }
                sharedPreference.getTheme() == 5 -> {
                    context.setTheme(R.style.ThemeFive)
                }
                sharedPreference.getTheme() == 6 -> {
                    context.setTheme(R.style.ThemeSix)
                }
                else -> {
                    context.setTheme(R.style.AppTheme)
                }
            }
        }
    }
}