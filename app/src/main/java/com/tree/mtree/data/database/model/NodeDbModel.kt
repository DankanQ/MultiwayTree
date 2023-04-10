package com.tree.mtree.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nodes")
data class NodeDbModel(
    @PrimaryKey(autoGenerate = true)
    val nodeId: Int,
    var name: String,
    val children: MutableList<NodeDbModel>,
    val parentId: Int
)