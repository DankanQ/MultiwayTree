package com.tree.mtree.data.database.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter
import com.tree.mtree.data.database.model.NodeDbModel
import javax.inject.Inject

class TypeConverter @Inject constructor() {
    @TypeConverter
    fun fromJson(json: String): MutableList<NodeDbModel> {
        val type = object : TypeToken<MutableList<NodeDbModel>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: MutableList<NodeDbModel>): String {
        return Gson().toJson(list)
    }
}