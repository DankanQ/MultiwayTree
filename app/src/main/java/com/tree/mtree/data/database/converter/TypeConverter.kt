package com.tree.mtree.data.database.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter
import com.tree.mtree.data.database.model.NodeDbModel
import javax.inject.Inject

class TypeConverter @Inject constructor() {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): List<NodeDbModel> {
        return gson.fromJson(json, object : TypeToken<List<NodeDbModel>>() {}.type)
    }

    @TypeConverter
    fun toJson(list: List<NodeDbModel>): String {
        return gson.toJson(list)
    }
}