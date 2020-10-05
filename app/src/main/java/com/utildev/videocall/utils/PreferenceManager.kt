package com.utildev.videocall.utils

import android.annotation.SuppressLint
import android.content.Context

class PreferenceManager(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(Constant.KEY_PREFERENCE, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, false)

    @SuppressLint("CommitPrefEdits")
    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? = sharedPreferences.getString(key, "")

    @SuppressLint("CommitPrefEdits")
    fun clearPreference() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}