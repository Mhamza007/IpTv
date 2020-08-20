package com.mhamza007.iptv.util

import android.content.Context

class SharedPreference(private var context: Context) {

    fun setRememberCredentials(remember: Boolean) {
        val rem = context.getSharedPreferences("remember", Context.MODE_PRIVATE)
        val editor = rem.edit()
        editor.putBoolean("remember", remember)
        editor.apply()
    }

    fun getRememberCredentials(): Boolean {
        val rem = context.getSharedPreferences("remember", Context.MODE_PRIVATE)
        return rem.getBoolean("remember", false)
    }

    fun setUserName(username: String) {
        val name = context.getSharedPreferences("username", Context.MODE_PRIVATE)
        val editor = name.edit()
        editor.putString("username", username)
        editor.apply()
    }

    fun getUserName(): String {
        val name = context.getSharedPreferences("username", Context.MODE_PRIVATE)
        return name.getString("username", "")!!
    }

    fun setPassword(password: String) {
        val pw = context.getSharedPreferences("password", Context.MODE_PRIVATE)
        val editor = pw.edit()
        editor.putString("password", password)
        editor.apply()
    }

    fun getPassword(): String {
        val pw = context.getSharedPreferences("password", Context.MODE_PRIVATE)
        return pw.getString("password", "")!!
    }

}