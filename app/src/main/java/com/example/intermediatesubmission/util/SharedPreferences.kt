package com.example.intermediatesubmission.util

import android.content.Context

class SharedPreferences(context: Context) {
    companion object {
        const val PREFS_NAME = "com.example.intermediatesubmission"
        const val PREFS_MODE = Context.MODE_PRIVATE
        const val PREFS_TOKEN = "token"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, PREFS_MODE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(PREFS_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(PREFS_TOKEN, null)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}