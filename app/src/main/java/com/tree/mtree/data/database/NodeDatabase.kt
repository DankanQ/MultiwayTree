package com.tree.mtree.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tree.mtree.data.database.converter.TypeConverter
import com.tree.mtree.data.database.model.NodeDbModel

@Database(entities = [NodeDbModel::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class NodeDatabase : RoomDatabase() {
    abstract fun nodeDao(): NodeDao

    companion object {
        private var INSTANCE: NodeDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "node_item.db"

        fun getInstance(application: Application): NodeDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    NodeDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}
