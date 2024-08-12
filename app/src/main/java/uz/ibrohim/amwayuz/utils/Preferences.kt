package uz.ibrohim.amwayuz.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences("amway_caches", Context.MODE_PRIVATE)
    }

    fun setPreference(preferences: SharedPreferences) {
        Preferences.preferences = preferences
    }

    var language: String
        get() = preferences.getString(Preferences::language.name, "")!!
        set(value) {
            preferences.edit().putString(Preferences::language.name, value).apply()
        }

    var uid: String
        get() = preferences.getString(Preferences::uid.name, "")!!
        set(value) {
            preferences.edit().putString(Preferences::uid.name, value).apply()
        }
    var name: String
        get() = preferences.getString(Preferences::uid.name, "")!!
        set(value) {
            preferences.edit().putString(Preferences::uid.name, value).apply()
        }
    var isAdmin: String
        get() = preferences.getString(Preferences::uid.name, "")!!
        set(value) {
            preferences.edit().putString(Preferences::uid.name, value).apply()
        }
}