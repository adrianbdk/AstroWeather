package com.example.astroweather

import android.content.Context

class SharedPreferencesData {
    companion object {
        private const val PREFERENCE = "defaultSharedPreference"

        fun saveString(context: Context, key: String?, value: String?) {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putString(key, value).apply()
        }

        fun save(context: Context, key: String?, value: Int) {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
        }

        fun saveBoolean(context: Context, key: String?, value: Boolean) {
            context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()
        }

        fun getBoolean(context: Context, key: String?): Boolean {
            return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(key, false)
        }

        fun getInt(context: Context, key: String?): Int {
            return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getInt(key, 0)
        }

        fun getString(context: Context, key: String?): String? {
            return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getString(key, null)
        }
    }
}