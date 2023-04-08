package com.example.treestructure.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tree.mtree.data.database.model.NodeDbModel

@Database(entities = [NodeDbModel::class], version = 1, exportSchema = false)
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
