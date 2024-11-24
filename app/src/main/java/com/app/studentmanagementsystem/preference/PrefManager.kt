package com.app.studentmanagementsystem.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class PrefManager(context: Context) {

    private var preferences: SharedPreferences
    private var editor: Editor
    private var PREF_NAME = "session"

    init {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            if (instance == null) {
                instance = PrefManager(context)
            }
            return instance!!
        }
    }

    fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun setString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return preferences.getString(key, defaultValue)
    }

    fun clear() {
        editor.clear().apply()
    }
}