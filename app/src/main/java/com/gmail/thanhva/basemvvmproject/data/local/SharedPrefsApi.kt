package com.gmail.thanhva.basemvvmproject.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.collection.LongSparseArray
import com.google.gson.Gson

/**
 *  Create by thanhva on 18/08/2020
 *  Class SharedPrefsApi
 */
class SharedPrefsApi constructor(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun set(key: String, value: String) = sharedPreferences.edit().apply { putString(key, value) }.apply()

    fun get(key: String, defValue: String): String = sharedPreferences.getString(key, defValue) ?: ""

    fun set(key: String, value: Int) = sharedPreferences.edit().apply { putInt(key, value) }.apply()

    fun get(key: String, defValue: Int) = sharedPreferences.getInt(key, defValue)

    fun set(key: String, value: Boolean) = sharedPreferences.edit().apply { putBoolean(key, value) }.apply()

    fun get(key: String, defValue: Boolean) = sharedPreferences.getBoolean(key, defValue)

    fun set(key: String, value: Long) = sharedPreferences.edit().apply { putLong(key, value) }.apply()

    fun get(key: String, defValue: Long) = sharedPreferences.getLong(key, defValue)

    fun clear() = sharedPreferences.edit().apply { clear() }.apply()

    fun remove(key: String) = sharedPreferences.edit().apply { remove(key) }.apply()

    fun <T> setList(key: String, list: List<T>) {
        val json = Gson().toJson(list)
        set(key, json)
    }

    fun setLongSparseArray(key: String, array: LongSparseArray<Boolean>) {
        val json = Gson().toJson(array)
        set(key, json)
    }

    fun contains(key: String) = sharedPreferences.contains(key)

    inline fun <reified T> setObject(key: String, value: T) = sharedPreferences.edit().apply {
        putString(key, Gson().toJson(value))
    }.apply()

    inline fun <reified T> getObject(key: String): T? = run {
        val data = get(key, "")
        return if (data.isEmpty()) {
            null
        } else {
            Gson().fromJson(data, T::class.java)
        }
    }
}