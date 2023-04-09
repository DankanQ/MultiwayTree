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

    @Query("UPDATE nodes SET name=:name WHERE id=:id")
    suspend fun updateNodeName(id: Int, name: String)

    @Query("DELETE FROM nodes WHERE parentId=:parentId AND id=:id")
    suspend fun deleteNode(parentId: Int, id: Int)

    @Query("SELECT * FROM nodes WHERE id=:id LIMIT 1")
    suspend fun getNode(id: Int): NodeDbModel

    @Query("SELECT * FROM nodes WHERE parentId=:parentId")
    suspend fun getNodes(parentId: Int): MutableList<NodeDbModel>
}