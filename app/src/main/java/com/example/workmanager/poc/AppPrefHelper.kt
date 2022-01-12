package com.example.workmanager.poc

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppPrefHelper {

    companion object {
        private var sharedPreferences: SharedPreferences? = null

        fun initPreferences(context: Context) {
            sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        }
    }

    fun getPreference(): SharedPreferences? {
        return sharedPreferences
    }

    fun clearPreferences() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    fun setTimePeriodsList(list: MutableList<TimePeriod>){
        val json = Gson().toJson(list)
        sharedPreferences?.edit()?.putString("time_periods", json)?.apply()
    }

    fun getTimePeriodsList(): MutableList<TimePeriod>?{
        val list = sharedPreferences?.getString("time_periods", null)
        val mutableListType = object : TypeToken<MutableList<TimePeriod>>() {}.type
        return Gson().fromJson(list, mutableListType)
    }
}