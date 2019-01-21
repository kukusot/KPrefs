package com.kukusot.kprefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object KPrefs {
    lateinit var preferences: SharedPreferences

    val supportedTypes = listOf(String::class, Int::class, Long::class, Boolean::class, Float::class)

    fun initialize(context: Context, prefsName: String = "prefs", prefsMode: Int = Context.MODE_PRIVATE) {
        with(context.applicationContext) {
            preferences = getSharedPreferences(prefsName, prefsMode)
        }
    }

    inline fun <reified T> ensureCorrectArgument() {
        if (!supportedTypes.contains(T::class)) {
            throw IllegalArgumentException("Unsupported type " + T::class.java.canonicalName)
        }
    }

    inline fun <reified T> get(prefName: String, defValue: T): T {
        ensureCorrectArgument<T>()

        with(preferences) {
            return when (defValue) {
                is String -> getString(prefName, defValue as String) as T
                is Int -> getInt(prefName, defValue as Int) as T
                is Long -> getLong(prefName, defValue as Long) as T
                is Boolean -> getBoolean(prefName, defValue as Boolean) as T
                is Float -> getFloat(prefName, defValue as Float) as T
                else -> throw IllegalArgumentException("Unsupported type " + T::class.java.canonicalName)
            }
        }
    }

    @SuppressLint("ApplySharedPref")
    inline fun <reified T> set(prefName: String, value: T, commit: Boolean = false) {
        ensureCorrectArgument<T>()

        with(preferences.edit()) {
            when (value) {
                is String -> putString(prefName, value as String)
                is Int -> putInt(prefName, value as Int)
                is Long -> putLong(prefName, value as Long)
                is Boolean -> putBoolean(prefName, value as Boolean)
                is Float -> putFloat(prefName, value as Float)
                else -> throw IllegalArgumentException("Unsupported type " + T::class.java.canonicalName)
            }

            if (commit) {
                commit()
            } else {
                apply()
            }
        }
    }
}