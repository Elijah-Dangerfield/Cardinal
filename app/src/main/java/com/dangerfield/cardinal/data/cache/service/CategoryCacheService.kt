package com.dangerfield.cardinal.data.cache.service

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.RawRes
import com.dangerfield.cardinal.domain.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type
import javax.inject.Inject

class CategoryCacheService (
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val resources: Resources
) {
    private val usersCategoriesKey = "users_categories_key"
    private val userHasSelectedCategoriesKey = "users_has_selected_categories_key"


    fun getUsersCategories(): List<Category> {
        return sharedPreferences.getString(usersCategoriesKey, null)?.let { json ->
            val type: Type = object : TypeToken<List<Category>>() {}.type
            return try {
                gson.fromJson(json, type)
            } catch (e: Exception) {
                listOf()
            }
        } ?: listOf()
    }

    fun getHasUserSelectedCategories() : Boolean {
        return sharedPreferences.getBoolean(userHasSelectedCategoriesKey, false)
    }

    fun setUsersCategories(list: List<Category>) {
        sharedPreferences.edit()
            .putString(usersCategoriesKey, gson.toJson(list))
            .apply()
    }

    fun setUserHasSelectedCategories() {
        sharedPreferences.edit().putBoolean(userHasSelectedCategoriesKey, true)
            .apply()
    }

    private inline fun <reified T> readRawJson(@RawRes rawResId: Int): T {
        resources.openRawResource(rawResId).bufferedReader().use {
            return gson.fromJson(it, object : TypeToken<T>() {}.type)
        }
    }
}