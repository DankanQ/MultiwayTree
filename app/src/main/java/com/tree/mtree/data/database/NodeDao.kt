package com.tree.mtree.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tree.mtree.data.database.model.NodeDbModel

@Dao
interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNode(nodeDbModel: NodeDbModel): Long

    @Query("UPDATE nodes SET name=:name WHERE nodeId=:nodeId")
    suspend fun updateNodeName(nodeId: Int, name: String)

    @Query("DELETE FROM nodes WHERE parentId=:parentId AND nodeId=:nodeId")
    suspend fun deleteNode(parentId: Int, nodeId: Int)

    @Query("SELECT * FROM nodes WHERE nodeId=:nodeId LIMIT 1")
    suspend fun getNode(nodeId: Int): NodeDbModel

    @Query("SELECT * FROM nodes WHERE parentId=:parentId")
    suspend fun getNodes(parentId: Int): List<NodeDbModel>
}