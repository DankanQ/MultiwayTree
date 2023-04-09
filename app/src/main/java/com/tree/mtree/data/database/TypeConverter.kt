package com.tree.mtree.data.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter
import com.tree.mtree.data.database.model.NodeDbModel

class TypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): MutableList<NodeDbModel> {
        val type = object : TypeToken<MutableList<NodeDbModel>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: MutableList<NodeDbModel>): String {
        return gson.toJson(list)
    }
}